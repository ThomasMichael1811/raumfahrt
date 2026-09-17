# Issue tracker: kabai

Issues and specs live on the **kabai** kanban board, project "Raumfahrt" (id 28). All access goes through the kabai MCP tools (`kabai_*`, clients may prefix them) — never SQL, never the filesystem.

## Conventions

- Search before create (`kabai_search_tickets`); comment on/merge near-duplicates instead of creating twins.
- Full descriptions: scope, references, effort estimate (XS–XL), observable acceptance criteria. Title-only tickets are not workable.
- Assign immediately (`kabai_assign_ticket`); never work unassigned tickets.
- One task per acceptance criterion (`kabai_add_task`); complete each immediately when met (`kabai_complete_task`), never batched.
- Work log via `kabai_add_comment` (pickup, decisions, blockers, completion with verification output).
- Moves only along the transition graph (`kabai_list_status_transitions`). Human questions → comment + `human_intervention` column.
- Epics group children via `kabai_link_tickets(parent_of)`; `blocks` expresses ordering.
- Knowledge lives in `kabai_docs_*` notes linked to tickets; `docs_required` tickets cannot close without a linked note.

## Session start

1. `kabai_list_projects` → project id 28
2. `kabai_list_board_statuses(28)` → column ids (per project, never reuse across projects)
3. `kabai_list_status_transitions(28)` → legal moves only

## When a skill says "publish to the issue tracker"

Search for duplicates, then `kabai_create_ticket(project_id: 28, status_id: <backlog>, title: ..., description: ...)`, add one task per acceptance criterion, assign immediately.

## When a skill says "fetch the relevant ticket"

`kabai_get_ticket_detailed(<id>)`; read `linked_notes`, run `kabai_docs_suggest_for_ticket`.
