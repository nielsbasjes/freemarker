/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package freemarker.template;

import freemarker.test.TemplateTest;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CollectionSubclassTest extends TemplateTest  {

    public static class User implements Comparable<User>{
        private final String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(name, user.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public int compareTo(User o) {
            if (this == o) return 0;
            if (o == null) return -1;
            return name.compareTo(o.name);
        }
    }

    // ------------------------------------------

    public static class Team {
        private final String name;

        public Team(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void testTeamName() throws Exception {
        Team team = new Team("Working");
        addToDataModel("team", team);
        assertEquals("Working", team.getName());
        assertOutput("${team.name}","Working");
    }

    // ------------------------------------------

    public static class TeamSet extends TreeSet<User> {
        private final String name;

        public TeamSet(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void testSubClassOfSet() throws Exception {
        TeamSet team = new TeamSet("Working");
        team.add(new User("a"));
        team.add(new User("b"));
        team.add(new User("c"));
        addToDataModel("team", team);
        assertOutput(
                "<#list team as user>${user.name}<#sep>, </#list>",
                "a, b, c");

        assertEquals("Working", team.getName());
        assertOutput(
                "${team.name}",
                "Working");
    }

    // ------------------------------------------

    public static class TeamList extends ArrayList<User> {
        private final String name;

        public TeamList(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void testSubClassOfList() throws Exception {
        TeamList team = new TeamList("Working");
        team.add(new User("a"));
        team.add(new User("b"));
        team.add(new User("c"));
        addToDataModel("team", team);
        assertOutput(
                "<#list team as user>${user.name}<#sep>, </#list>",
                "a, b, c");

        assertEquals("Working", team.getName());
        assertOutput(
                "${team.name}",
                "Working");
    }

    // ------------------------------------------

    public static class TeamMap extends TreeMap<String, User> {
        private final String name;

        public TeamMap(String name) {
            this.name = name;
        }

        public void add(User newUser) {
            put(newUser.name, newUser);
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void testSubClassOfMap() throws Exception {
        TeamMap team = new TeamMap("Working");
        team.add(new User("a"));
        team.add(new User("b"));
        team.add(new User("c"));
        addToDataModel("team", team);
        assertOutput(
                "<#list team?keys as user>${user}->${team[user].name}<#sep>, </#list>",
                "a->a, b->b, c->c");

        assertEquals("Working", team.getName());
        assertOutput(
                "${team.name}",
                "Working");
    }

}
