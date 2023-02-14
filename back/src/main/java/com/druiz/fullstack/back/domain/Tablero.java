package com.druiz.fullstack.back.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un tablero de juego de 4 en raya.
 */
@Table(name = "Tableros")
@Data
@NoArgsConstructor
public class Tablero {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;
   private int filas;
   private int columnas;
   private Ficha[][] fichas;

   // Debemos inicializar la lista "jugadores" para evitar un NullPointerException
   private List<Jugador> jugadores = new ArrayList<>();

   private String status;

   /**
    * Crea un nuevo tablero de juego de 4 en raya.
    *
    * @param filas    el número de filas del tablero
    * @param columnas el número de columnas del tablero
    */
   public Tablero(int filas, int columnas, List<Jugador> jugadores) {
      this.filas = filas;
      this.columnas = columnas;
      this.fichas = new Ficha[filas][columnas];
      if (this.jugadores.size() == 1) {
         this.status = "Game not started";
      } else if (this.jugadores.size() == 2) {
         this.status = "Game completed! but not finished";
      }
      this.jugadores = jugadores;
   }

   /**
    * Establece las fichas en el tablero inicializando una matriz de Ficha.
    */
   public void setFichas() {
      this.fichas = new Ficha[filas][columnas];
      for (int i = 0; i < filas; i++) {
         for (int j = 0; j < columnas; j++) {
            this.fichas[i][j] = new Ficha();
         }
      }
   }

   /**
    * Realiza un movimiento en el 4 en raya.
    *
    * @param fila    la fila en la que se desea colocar la ficha
    * @param columna la columna en la que se desea colocar la ficha
    * @param jugador el jugador que realiza el movimiento
    * @return true si el movimiento se realiza con éxito, false en caso contrario
    */
   public boolean realizarMovimiento(int fila, int columna, Jugador jugador) {
      // Verifica que la fila y columna estén dentro del rango permitido
      if (fila < 0 || fila >= this.filas || columna < 0 || columna >= this.columnas) {
         return false;
      }

      // Verifica que la posición esté disponible
      if (!this.fichas[fila][columna].isDisponible()) {
         return false;
      }

      // Asigna la ficha al jugador en la posición especificada
      this.fichas[fila][columna].setJugador(jugador);
      this.fichas[fila][columna].setDisponible(false);

      // Verifica si el movimiento resultó en una victoria
      if (esGanador(fila, columna)) {
         this.status = "Game completed, the winner is: " + jugador.getUserPlayer();
         return true;
      }

      return true;
   }

   /**
    * Determina si un jugador ha ganado.
    *
    * @param fila    la fila del movimiento
    * @param columna la columna del movimiento
    * @return true si el jugador ha ganado, false en caso contrario
    */
   public boolean esGanador(int fila, int columna) {
      return verificarFilaCompleta(fila) ||
              verificarColumnaCompleta(columna) ||
              verificarDiagonalIzquierdaDerecha(fila, columna) ||
              verificarDiagonalDerechaIzquierda(fila, columna);
   }

    /**
     * Verifica si una fila está completa.
     *
     * @param fila la fila a verificar
     * @return true si la fila está completa, false en caso contrario
     */
    private boolean verificarFilaCompleta(int fila) {
       for (int i = 0; i < this.columnas - 3; i++) {
          // Se verifica que el jugador de la primera casilla sea diferente de nulo y que sea igual al jugador
          // de las tres siguientes casillas consecutivas.
          if (this.fichas[fila][i].getJugador() != null &&
                  this.fichas[fila][i].getJugador() == this.fichas[fila][i + 1].getJugador() &&
                  this.fichas[fila][i + 1].getJugador() == this.fichas[fila][i + 2].getJugador() &&
                  this.fichas[fila][i + 2].getJugador() == this.fichas[fila][i + 3].getJugador()) {
             return true;
          }
       }
       return false;
    }

    /**
     * Verifica si una columna está completa.
     *
     * @param columna la columna a verificar
     * @return true si la columna está completa, false en caso contrario
     */
    private boolean verificarColumnaCompleta(int columna) {
       for (int i = 0; i < this.filas - 3; i++) {
          // Verifica si existe una secuencia de cuatro fichas consecutivas con el mismo jugador en la columna
          if (this.fichas[i][columna].getJugador() != null &&
                  this.fichas[i][columna].getJugador() == this.fichas[i + 1][columna].getJugador() &&
                  this.fichas[i + 1][columna].getJugador() == this.fichas[i + 2][columna].getJugador() &&
                  this.fichas[i + 2][columna].getJugador() == this.fichas[i + 3][columna].getJugador()) {
             // Si existe, retorna verdadero
             return true;
          }
       }
       // Si no se encuentra una secuencia de cuatro fichas consecutivas con el mismo jugador, retorna falso
       return false;
    }


    /**
      * Verifica si una diagonal de izquierda a derecha está completa.
      *
      * Esta función recorre cada cuarteto de casillas consecutivas en la diagonal. Si encuentra un cuarteto con el mismo jugador,
      * retorna verdadero, indicando que ese jugador ha ganado.
      *
      * @param fila la fila a verificar
      * @param columna la columna a verificar
      * @return true si la diagonal está completa, false en caso contrario
      */
   private boolean verificarDiagonalIzquierdaDerecha(int fila, int columna) {
      // Primero, determinamos el número mínimo de filas y columnas que debemos recorrer para asegurarnos
      // de revisar todas las diagonales que contienen la posición (fila, columna).
      int minFila = Math.max(0, fila - (columnas - 1 - columna));
      int minColumna = Math.max(0, columna - (filas - 1 - fila));

      // Luego, recorremos cada diagonal que contiene la posición (fila, columna), comparando cada cuarteto
      // de casillas consecutivas en la diagonal. Si encontramos un cuarteto de casillas con el mismo jugador,
      // retornamos verdadero, indicando que ese jugador ha ganado.
      for (int i = minFila, j = minColumna; i <= filas - 4 && j <= columnas - 4; i++, j++) {
         if (fichas[i][j].getJugador() != null &&
                 fichas[i][j].getJugador() == fichas[i + 1][j + 1].getJugador() &&
                 fichas[i + 1][j + 1].getJugador() == fichas[i + 2][j + 2].getJugador() &&
                 fichas[i + 2][j + 2].getJugador() == fichas[i + 3][j + 3].getJugador()) {
            return true;
         }
      }

      return false;
   }

    /**
      * Verifica si una diagonal de derecha a izquierda está completa.
      *
      * Esta función recorre cada cuarteto de casillas consecutivas en la diagonal. Si encuentra un cuarteto con el mismo jugador,
      * retorna verdadero, indicando que ese jugador ha ganado.
      *
      * @param fila la fila a verificar
      * @param columna la columna a verificar
      * @return true si la diagonal está completa, false en caso contrario
      */
   private boolean verificarDiagonalDerechaIzquierda(int fila, int columna) {
      // Primero, determinamos el número mínimo de filas y columnas que debemos recorrer para asegurarnos
      // de revisar todas las diagonales que contienen la posición (fila, columna).
      int minFila = Math.max(0, fila - columna);
      int minColumna = Math.min(columnas - 1, columna + fila);

      /* Luego, recorremos cada diagonal que contiene la posición (fila, columna), comparando cada cuarteto
       de casillas consecutivas en la diagonal. Si encontramos un cuarteto de casillas con el mismo jugador,
       retornamos verdadero, indicando que ese jugador ha ganado.
       */
      for (int i = minFila, j = minColumna; i <= filas - 1 && j >= 0; i++, j--) {
         if (fichas[i][j].getJugador() != null &&
                 fichas[i][j].getJugador().equals(fichas[i + 1][j - 1].getJugador()) &&
                 fichas[i][j].getJugador().equals(fichas[i + 2][j - 2].getJugador()) &&
                 fichas[i][j].getJugador().equals(fichas[i + 3][j - 3].getJugador())) {
            return true;
         }
      }
      return false;
   }


   /**
    * Añade un jugador al tablero si el ID del jugador no es nula.
    *
    * @param jugador El jugador a añadir.
    */
   public void addJugador(Jugador jugador) {
      if (jugador.getId() != null) {
         this.jugadores.add(jugador);
      }
   }

   /**
    * Elimina un jugador del tablero si el ID del jugador no es nula.
    *
    * @param jugador El jugador a eliminar.
    */
   protected void removeJugador(Jugador jugador) {
      if (jugador.getId() != null) {
         this.jugadores.remove(jugador);
      }
   }

}
