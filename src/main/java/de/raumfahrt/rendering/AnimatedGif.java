package de.raumfahrt.rendering;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.Node;

public final class AnimatedGif {

    private final List<Frame> frames;
    private final int durationMillis;

    private AnimatedGif(List<Frame> frames) {
        this.frames = List.copyOf(frames);
        durationMillis = frames.stream().mapToInt(Frame::durationMillis).sum();
    }

    public static AnimatedGif load(String resourcePath) {
        try (InputStream input = AnimatedGif.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException("GIF-Ressource nicht gefunden: " + resourcePath);
            }
            return decode(input);
        } catch (IOException exception) {
            throw new IllegalStateException("GIF-Ressource konnte nicht geladen werden: " + resourcePath, exception);
        }
    }

    public BufferedImage frameAt(long elapsedMillis) {
        int frameTime = (int) Math.floorMod(elapsedMillis, durationMillis);
        for (Frame frame : frames) {
            if (frameTime < frame.durationMillis()) {
                return frame.image();
            }
            frameTime -= frame.durationMillis();
        }
        return frames.get(0).image();
    }

    public int frameCount() {
        return frames.size();
    }

    public int frameDurationMillis(int index) {
        return frames.get(index).durationMillis();
    }

    private static AnimatedGif decode(InputStream input) throws IOException {
        try (ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new IllegalStateException("GIF-Ressource enthält kein lesbares Bild");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, false, false);
                return decodeFrames(reader);
            } finally {
                reader.dispose();
            }
        }
    }

    private static AnimatedGif decodeFrames(ImageReader reader) throws IOException {
        int count = reader.getNumImages(true);
        IIOMetadata streamMetadata = reader.getStreamMetadata();
        int width = metadataDimension(streamMetadata, "logicalScreenWidth", reader.getWidth(0));
        int height = metadataDimension(streamMetadata, "logicalScreenHeight", reader.getHeight(0));
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        List<Frame> frames = new ArrayList<>(count);
        Rectangle previousBounds = null;
        String previousDisposal = "none";
        BufferedImage previousCanvas = null;
        for (int index = 0; index < count; index++) {
            canvas = applyDisposal(canvas, previousBounds, previousDisposal, previousCanvas);
            DecodedFrame decoded = drawFrame(reader, index, canvas);
            canvas = decoded.canvas();
            frames.add(decoded.frame());
            previousBounds = decoded.bounds();
            previousDisposal = decoded.disposal();
            previousCanvas = decoded.restoreCanvas();
        }
        return new AnimatedGif(frames);
    }

    private static BufferedImage applyDisposal(
            BufferedImage canvas, Rectangle bounds, String disposal, BufferedImage previousCanvas) {
        if ("restoreToBackgroundColor".equals(disposal) && bounds != null) {
            clear(canvas, bounds);
        } else if ("restoreToPrevious".equals(disposal) && previousCanvas != null) {
            return copy(previousCanvas);
        }
        return canvas;
    }

    private static DecodedFrame drawFrame(ImageReader reader, int index, BufferedImage canvas) throws IOException {
        Node root = reader.getImageMetadata(index).getAsTree("javax_imageio_gif_image_1.0");
        Node descriptor = child(root, "ImageDescriptor");
        int left = attribute(descriptor, "imageLeftPosition", 0);
        int top = attribute(descriptor, "imageTopPosition", 0);
        int frameWidth = attribute(descriptor, "imageWidth", reader.getWidth(index));
        int frameHeight = attribute(descriptor, "imageHeight", reader.getHeight(index));
        Node control = child(root, "GraphicControlExtension");
        String disposal = attribute(control, "disposalMethod", "none");
        int delayMillis = Math.max(10, attribute(control, "delayTime", 0) * 10);
        BufferedImage restoreCanvas = "restoreToPrevious".equals(disposal) ? copy(canvas) : null;
        Graphics2D graphics = canvas.createGraphics();
        graphics.drawImage(reader.read(index), left, top, null);
        graphics.dispose();
        return new DecodedFrame(
                canvas,
                new Frame(copy(canvas), delayMillis),
                new Rectangle(left, top, frameWidth, frameHeight),
                disposal,
                restoreCanvas);
    }

    private static int metadataDimension(IIOMetadata metadata, String name, int fallback) {
        if (metadata == null) {
            return fallback;
        }
        Node root = metadata.getAsTree("javax_imageio_gif_stream_1.0");
        return attribute(child(root, "LogicalScreenDescriptor"), name, fallback);
    }

    private static Node child(Node parent, String name) {
        if (parent == null) {
            return null;
        }
        for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (name.equals(node.getNodeName())) {
                return node;
            }
        }
        return null;
    }

    private static int attribute(Node node, String name, int fallback) {
        if (node == null || node.getAttributes() == null || node.getAttributes().getNamedItem(name) == null) {
            return fallback;
        }
        return Integer.parseInt(node.getAttributes().getNamedItem(name).getNodeValue());
    }

    private static String attribute(Node node, String name, String fallback) {
        if (node == null || node.getAttributes() == null || node.getAttributes().getNamedItem(name) == null) {
            return fallback;
        }
        return node.getAttributes().getNamedItem(name).getNodeValue();
    }

    private static BufferedImage copy(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return copy;
    }

    private static void clear(BufferedImage canvas, Rectangle bounds) {
        Graphics2D graphics = canvas.createGraphics();
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fill(bounds);
        graphics.dispose();
    }

    private record Frame(BufferedImage image, int durationMillis) {}

    private record DecodedFrame(
            BufferedImage canvas, Frame frame, Rectangle bounds, String disposal, BufferedImage restoreCanvas) {}
}
