package ru.javaops.masterjava.matrix;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] res = new int[matrixSize][matrixSize];
        ArrayDeque<Future<int[]>> futures = new ArrayDeque<>();

        for (int xB = 0; xB < matrixSize; xB++) {
            int[] bRow = new int[matrixSize];
            for (int index = 0; index < matrixSize; index++)
                bRow[index] = matrixB[index][xB];
            futures.add(executor.submit(new Task(bRow, matrixA, xB)));
        }

        while (!futures.isEmpty()) {
            if (futures.peekFirst().isDone()) {
                final int[] ints = futures.pollFirst().get();
                res[ints[0]] = Arrays.copyOfRange(ints, 1, matrixSize + 1);
            }
        }

        for (int x = 0; x < matrixSize; x++)
            for (int y = 0; y < matrixSize; y++)
                matrixC[x][y] = res[y][x];


//        System.out.println("m------" + Arrays.deepToString(matrixC));

        return matrixC;
    }

    private static class Task implements Callable<int[]> {
        final int[] bRow;
        final int[][] aMatrix;
        final int size;
        final int[] result;

        public Task(int[] bRow, int[][] aMatrix, int num) {
            this.size = bRow.length;
            this.result = new int[size + 1];
            this.result[0] = num;
            this.aMatrix = aMatrix;
            this.bRow = bRow;
        }

        @Override
        public int[] call() throws Exception {
            for (int a = 0; a < size; a++) {
                int[] aRow = aMatrix[a];
                int sum = 0;
                for (int i = 0; i < size; i++)
                    sum += aRow[i] * bRow[i];
                result[a + 1] = sum;
            }

            return result;
        }
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] aRow, bRow = new int[matrixSize];

        for (int xB = 0; xB < matrixSize; xB++) {
            for (int index = 0; index < matrixSize; index++)
                bRow[index] = matrixB[index][xB];

            for (int yA = 0; yA < matrixSize; yA++) {
                aRow = matrixA[yA];
                int sum = 0;
                for (int i = 0; i < matrixSize; i++) {
                    sum += aRow[i] * bRow[i];
                }
                matrixC[yA][xB] = sum;
            }
        }
//        System.out.println("s------" + Arrays.deepToString(matrixC));

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
