package ru.javaops.masterjava.matrix;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] matrixBLine = new int[matrixSize];
        List<Future> futures = new LinkedList<>();

        for (int i = 0; i < matrixSize; i++) {
            for (int idx = 0; idx < matrixSize; idx++) {
                matrixBLine[idx] = matrixB[idx][i];
            }
            int finalI = i;
            futures.add(executor.submit(new Runnable() {
                int[] line = Arrays.copyOf(matrixBLine, matrixSize);

                @Override
                public void run() {
                    for (int j = 0; j < matrixSize; j++) {
                        int sum = 0;
                        for (int k = 0; k < matrixSize; k++) {
                            sum += matrixA[j][k] * line[k];
                        }
                        matrixC[j][finalI] = sum;
                    }
                }
            }));
        }
        for (Future future : futures) {
            future.get();
        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] matrixBLine = new int[matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int idx = 0; idx < matrixSize; idx++) {
                matrixBLine[idx] = matrixB[idx][i];
            }
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[j][k] * matrixBLine[k];
                }
                matrixC[j][i] = sum;
            }
        }
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