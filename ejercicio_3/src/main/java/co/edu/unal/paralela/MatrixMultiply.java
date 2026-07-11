package co.edu.unal.paralela;

import static edu.rice.pcdp.PCDP.forallChunked;
import static edu.rice.pcdp.PCDP.forseq2d;

/**
 * Clase envolvente pata implementar de forma eficiente la multiplicación dde matrices en paralelo.
 */
public final class MatrixMultiply {

    final static int chunksNumber = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * Constructor por omisión.
     */
    private MatrixMultiply() {
    }

    /**
     * Realiza una multiplicación de matrices bidimensionales (A x B = C) de forma secuencial.
     *
     * @param A Una matriz de entrada con dimensiones NxN
     * @param B Una matriz de entrada con dimensiones NxN
     * @param C Matriz de salida
     * @param N Tamaño de las matrices de entrada
     */
    public static void seqMatrixMultiply(final double[][] A, final double[][] B,
            final double[][] C, final int N) {
        forseq2d(0, N - 1, 0, N - 1, (i, j) -> {
            C[i][j] = 0.0;
            for (int k = 0; k < N; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        });
    }

    /**
     * @param A Una matriz de entrada con dimensiones NxN
     * @param B Una matriz de entrada con dimensiones NxN
     * @param C Matriz de salida
     * @param N Tamaño de las matrices de entrada
     */
    public static void parMatrixMultiply(final double[][] A, final double[][] B,
            final double[][] C, final int N) {
        
        // El tamaño de bloque (chunk size) para la distribución de hilos
        final int chunkSize = Math.max(1, N / chunksNumber);

        forallChunked(0, N - 1, chunkSize, i -> {
            for (int j = 0; j < N; j++) {
                C[i][j] = 0.0;
            }
            
            for (int k = 0; k < N; k++) {
                double aValue = A[i][k];
                for (int j = 0; j < N; j++) {
                    C[i][j] += aValue * B[k][j];
                }
            }
        });
    }
}