package com.example.mediaservice.Service;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisSession {
    private List<String> conversationHistory = new ArrayList<>();

    public void addInteraction(String question, String userResponse) {
        conversationHistory.add("Q: " + question);
        conversationHistory.add("A: " + userResponse);
    }

    public List<String> getHistory() {
        return conversationHistory;
    }
}
