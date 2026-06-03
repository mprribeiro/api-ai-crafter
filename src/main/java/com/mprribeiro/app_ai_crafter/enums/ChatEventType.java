package com.mprribeiro.app_ai_crafter.enums;

public enum ChatEventType {
    THOUGHT,      // "Thought for 2s"
    MESSAGE,      // Standard conversational text
    FILE_EDIT,    // Code generation <file>
    TOOL_LOG      // "Reading file..." <tool>
}
