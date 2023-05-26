package com.dynamis.views;

import java.util.List;
import java.util.Map;

public class DisplayTeamsView {

    public void show(Map<String, List<String>> teamMembers) {
        int i = 1;

        for(Map.Entry<String, List<String>> entry : teamMembers.entrySet()) {
            String teamName = entry.getKey();
            List<String> members = entry.getValue();
        
            System.out.printf("\n> %d. %s\n", i, teamName);

            if(members.size() == 0) {
                System.out.println("    ---");
                continue;
            }

            for (String member : members) {
                System.out.println("    " + member);
            }
            i++;
        }
    }

}
