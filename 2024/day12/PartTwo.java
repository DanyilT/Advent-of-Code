// Day 12: Garden Groups

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/12/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int rows = lines.size();
            int cols = lines.getFirst().length();
            Plant[] characterMap = new Plant[rows * cols];

            int c = 0;
            for (String line : lines) {
                if (line.isBlank()) continue;
                for (String character : line.split("")) {
                    characterMap[c] = new Plant(character, c++);
                }
            }

            // Calculate positions and walls
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    calcPos(characterMap, j, i, cols, rows);
                }
            }

            // Merge plants until no changes happen
            boolean merged = true;
            while (merged) {
                merged = false;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (merge(characterMap, j, i, cols, rows)) {
                            merged = true;
                        }
                    }
                }
            }

            Set<Plant> allPlants = new HashSet<>();
            Collections.addAll(allPlants, characterMap);

            Integer totalPrice = 0;
            for (Plant p : allPlants) {
                totalPrice += p.getCalcObj();
            }

            System.out.printf("Total Price (Area * Walls): %d\n", totalPrice);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean merge(Plant[] characterMap, int x, int y, int cols, int rows) {
        boolean merged = false;
        Plant plant = characterMap[y * cols + x];
        if (x > 0 && characterMap[y * cols + x - 1].getKey().equals(plant.getKey())) {
            if (!characterMap[y * cols + x - 1].equals(plant)) {
                plant = plant.merge(characterMap[y * cols + x - 1]);
                characterMap[y * cols + x] = plant;
                characterMap[y * cols + x - 1] = plant;
                merged = true;
            }
        }
        if (y > 0 && characterMap[(y - 1) * cols + x].getKey().equals(plant.getKey())) {
            if (!characterMap[(y - 1) * cols + x].equals(plant)) {
                plant = plant.merge(characterMap[(y - 1) * cols + x]);
                characterMap[y * cols + x] = plant;
                characterMap[(y - 1) * cols + x] = plant;
                merged = true;
            }
        }
        return merged;
    }

    private static void calcPos(Plant[] characterMap, int x, int y, int cols, int rows) {
        Plant plant = characterMap[y * cols + x];
        plant.incArea();

        // Add walls around the plant
        if (x < cols - 1 && !characterMap[y * cols + x + 1].getKey().equals(plant.getKey())) {
            plant.addWall(new Wall(Wall.RIGHT, x, y));
            plant.incWalls();
        }
        if (x > 0 && !characterMap[y * cols + x - 1].getKey().equals(plant.getKey())) {
            plant.addWall(new Wall(Wall.LEFT, x, y));
            plant.incWalls();
        }
        if (y < rows - 1 && !characterMap[(y + 1) * cols + x].getKey().equals(plant.getKey())) {
            plant.addWall(new Wall(Wall.BOTTOM, x, y));
            plant.incWalls();
        }
        if (y > 0 && !characterMap[(y - 1) * cols + x].getKey().equals(plant.getKey())) {
            plant.addWall(new Wall(Wall.TOP, x, y));
            plant.incWalls();
        }

        // Add boundary walls
        if (x == 0) {
            plant.addWall(new Wall(Wall.LEFT, x, y));
            plant.incWalls();
        }
        if (y == 0) {
            plant.addWall(new Wall(Wall.TOP, x, y));
            plant.incWalls();
        }
        if (x == cols - 1) {
            plant.addWall(new Wall(Wall.RIGHT, x, y));
            plant.incWalls();
        }
        if (y == rows - 1) {
            plant.addWall(new Wall(Wall.BOTTOM, x, y));
            plant.incWalls();
        }
    }

    static class Plant {
        private Set<Plant> plants = new HashSet<>();
        private Set<Wall> wallObjs = new HashSet<>();
        private int id;
        private String character;
        private int area = 0;
        private int walls = 0;

        public Plant(String character, int id) {
            this.plants.add(this);
            this.character = character;
            this.id = id;
        }

        public Integer getCalcObj() {
            int newArea = 0;
            int newWalls = 0;
            for (Plant p : this.plants) {
                newArea += p.area;
            }

            while (!wallObjs.isEmpty()) {
                Wall w = wallObjs.iterator().next();
                Set<Wall> toRemove = new HashSet<>();
                if (w.wallSide == Wall.TOP || w.wallSide == Wall.BOTTOM) {
                    toRemove.add(w);
                    toRemove.addAll(getWalls(w, 1, 0));
                    toRemove.addAll(getWalls(w, -1, 0));
                    newWalls++;
                } else {
                    toRemove.add(w);
                    toRemove.addAll(getWalls(w, 0, 1));
                    toRemove.addAll(getWalls(w, 0, -1));
                    newWalls++;
                }
                wallObjs.removeAll(toRemove);
            }

            return newArea * newWalls;
        }

        private Collection<Wall> getWalls(Wall wall, int dx, int dy) {
            Set<Wall> toRemove = new HashSet<>();
            for (Wall w : wallObjs) {
                if (w.x == wall.x + dx && w.y == wall.y + dy && w.wallSide == wall.wallSide) {
                    toRemove.add(w);
                    toRemove.addAll(getWalls(w, dx, dy));
                    break;
                }
            }
            return toRemove;
        }

        public void incArea() {
            area++;
        }

        public void incWalls() {
            walls++;
        }

        public void addWall(Wall w) {
            wallObjs.add(w);
        }

        public String getKey() {
            return character;
        }

        public Set<Plant> getPlants() {
            return plants;
        }

        public Plant merge(Plant plant) {
            if (this.id > plant.id) {
                plant.wallObjs.addAll(this.wallObjs);
                plant.plants.addAll(this.getPlants());
                return plant;
            } else {
                this.wallObjs.addAll(plant.wallObjs);
                this.plants.addAll(plant.getPlants());
                return this;
            }
        }
    }

    static class Wall {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int TOP = 2;
        public static final int BOTTOM = 3;

        int wallSide;
        int x;
        int y;

        public Wall(int wallSide, int x, int y) {
            this.wallSide = wallSide;
            this.x = x;
            this.y = y;
        }
    }
}
