#!/bin/bash
set -euo pipefail
IFS=$'\n\t'
# PreToolUse/Bash hook: block code-discovery bash commands; redirect to codebase-memory or serena.

HOOK_JSON=$(cat)
COMMAND=$(echo "$HOOK_JSON" | jq -r '.toolCall.args.CommandLine // .toolCall.args.command // .toolCall.arguments.CommandLine // .toolCall.arguments.command // .arguments.CommandLine // .tool_input.command // ""' 2>/dev/null) || COMMAND=""

if [[ -z "$COMMAND" ]]; then
    exit 0
fi

NORMALIZED="${COMMAND#rtk }"

if echo "$NORMALIZED" | grep -qE '(grep|find|cat|head|tail|sed|awk|read|smart)\b.+\.(kt|java|kts)\b|(grep|find)\b.*(kotlin|java)'; then
    REASON="BLOCKED: Use codebase-memory-mcp for code search (search_graph, search_code, get_code_snippet, trace_path) or serena MCP (find_symbol, find_declaration, find_referencing_symbols, find_implementations). Bash grep/find/cat/rtk allowed only for config/XML/text files. Retry if this was a non-code file."
    jq -n --arg reason "$REASON" '{
      "permissionDecision": "deny",
      "permissionDecisionReason": $reason,
      "decision": "deny",
      "reason": $reason,
      "hookSpecificOutput": {
        "hookEventName": "PreToolUse",
        "permissionDecision": "deny",
        "permissionDecisionReason": $reason,
        "decision": "deny",
        "reason": $reason
      }
    }'
    exit 2
fi

exit 0
