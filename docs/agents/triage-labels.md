# Triage Labels

The skills speak in terms of five canonical triage roles. kabai has no label vocabulary — only board columns — so each role maps to a column (or practice):

| Label in mattpocock/skills | In kabai (project 28) | Meaning |
| -------------------------- | --------------------- | ------- |
| `needs-triage`             | column `backlog`      | Maintainer needs to evaluate this ticket |
| `needs-info`               | column `backlog` + work-log question | Waiting on reporter for more information |
| `ready-for-agent`          | column `ready`        | Fully specified, ready for an AFK agent |
| `ready-for-human`          | column `human_intervention` | Requires human decision |
| `wontfix`                  | work-log comment "won't fix", ticket stays out of working columns | Will not be actioned |

When a skill says "apply the AFK-ready triage label" or similar, perform the matching kabai column move (`kabai_move_ticket`) instead of applying a label.
