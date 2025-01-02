package project.model.util;

import project.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    public static final Vector2d DEFAULT_START_POSITION = new Vector2d(0, 0);
    private final List<Vector2d> allPositions;
    private final int elementsCount;

    public RandomPositionGenerator(int width, int height, int elementsCount){
        this.elementsCount = elementsCount;
        this.allPositions = this.generatePositions(RandomPositionGenerator.DEFAULT_START_POSITION, width, height);
    }

    public List<Vector2d> generatePositions(Vector2d startPosition, int width, int height) {
        var allPositions = new ArrayList<Vector2d>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                var position = new Vector2d(startPosition.getX() + i, startPosition.getY() + j);
                allPositions.add(position);
            }
        }
        return allPositions;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new RandomPositionGeneratorIterator(this.allPositions, this.elementsCount);
    }

    private static class RandomPositionGeneratorIterator implements Iterator<Vector2d> {
        private int toGenerateCounter;
        private final List<Vector2d> allPositions;

        public RandomPositionGeneratorIterator(List<Vector2d> allPositions, int toGenerateCounter) {
            this.allPositions = allPositions;
            this.toGenerateCounter = toGenerateCounter;
        }

        @Override
        public boolean hasNext() {
            return this.toGenerateCounter > 0;
        }

        @Override
        public Vector2d next() {
            Random random = new Random();
            int randomIndex = random.nextInt(this.allPositions.size());
            var randomPosition = this.allPositions.get(randomIndex);
            this.allPositions.remove(randomIndex);
            this.toGenerateCounter--;

            return randomPosition;
        }
    }
}
