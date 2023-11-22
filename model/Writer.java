/* ***************************************************************
* Autor............: Joao Victor Gomes Macedo
* Matricula........: 202210166
* Inicio...........: 09/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Writer
* Funcao...........: Modela o comportamento dos escritores
*************************************************************** */
package model;
import controller.MainController;
import javafx.application.Platform;
import java.util.Random;
public class Writer extends Thread{
  private int id;//id do escritor
  private MainController control;//instancia do controle para alteracao de imagens
  private volatile boolean isPaused = false;//flag para pausar o processo
  
  /* ***************************************************************
  * Metodo: Writer
  * Funcao: Construtor do objeto Writer.
  * Parametros: Id do professor e o controlador da interface.
  * Retorno: Sem retorno.
  *************************************************************** */
  public Writer(int n, MainController control){
    this.id = n;
    this.control = control;
  }

  /* ***************************************************************
  * Metodo: run
  * Funcao: Executar o ciclo de vida do escritor.
  * Parametros: Sem parametros.
  * Retorno: Sem retorno.
  *************************************************************** */
  @Override
  public void run() {
    while (true){
      try {
        thinkUpData();//pensa o dado
        MainController.db.acquire();//adquire o semaforo db
        writeDataBase();//escreve o dado
        MainController.db.release();//libera o semaforo db
      } catch (InterruptedException e) {}
    }
  }

  /* ***************************************************************
  * Metodo: writeDataBase
  * Funcao: Realiza o estágio de escrever na base de dados.
  * Parametros: Sem parametros.
  * Retorno: Sem retorno.
  *************************************************************** */
  private void writeDataBase() {
    Platform.runLater(()-> control.changeProfessorWritedb(this.id));//altera a imagem
    if (this.id>0) {//verifica qual processo esta alterando o label
      Platform.runLater(()-> control.setDataBaseText(control.getDataBaseText()+"\n"+avisoAleatorio()));
    }
    else{//caso seja o processo id 0, substitui todo o dado por um unico
      Platform.runLater(()-> control.setDataBaseText(avisoAleatorio()));
    }
    sleepTime(control.writingData(this.id));//processo dorme por um determinado tempo
    pausando();//caso seja pausado,o processo entrara em espera ocupada mantendo o seu estado atual
  }

  /* ***************************************************************
  * Metodo: thinkUpData
  * Funcao: Realiza o estágio de pensar no dado a ser escrito.
  * Parametros: Sem parametros.
  * Retorno: Sem retorno.
  *************************************************************** */
  private void thinkUpData() {
    Platform.runLater(()-> control.changeProfessorThink(this.id));//altera a imagem
    sleepTime(control.thinkingData(this.id));//processo dorme por um determinado tempo
    pausando();//caso seja pausado,o processo entrara em espera ocupada mantendo o seu estado atual
  }
  
  /* ***************************************************************
  * Metodo: sleepTime
  * Funcao: Faz o processo dormir.
  * Parametros: Valor numerico do tipo inteiro.
  * Retorno: Sem retorno.
  *************************************************************** */
  public void sleepTime(int time){
    try {
      sleep((long) time*1000);
    } catch (InterruptedException e) {
    }
  }
  
  /* ***************************************************************
  * Metodo: pausando
  * Funcao: pausa o processo.
  * Parametros: Sem parametro.
  * Retorno: Sem retorno.
  *************************************************************** */
  private void pausando(){
    while (isPaused && !Thread.interrupted()){
      sleepTime(1);
    }
  }

  /* ***************************************************************
  * Metodo: pausar
  * Funcao: Modifica a flag responsavel por pausar o processo para
  *         que este seja pausado.
  * Parametros: Sem parametro.
  * Retorno: Sem retorno.
  *************************************************************** */
  public void pausar(){
    isPaused = true;
  }

  /* ***************************************************************
  * Metodo: retomar
  * Funcao: Modifica a flag responsavel por pausar o processo para
  *         que este seja retomado.
  * Parametros: Sem parametro.
  * Retorno: Sem retorno.
  *************************************************************** */
  public void retomar(){
    isPaused = false;
  }
  
   /* ***************************************************************
  * Metodo: avisoAleatorio
  * Funcao: Seleciona aleatoriamente uma string do vetor e retorna
  *	    para que seja usado como dado pelo escritor.
  * Parametros: Sem parametro.
  * Retorno: Sem retorno.
  *************************************************************** */
  public static String avisoAleatorio() {
    String[] frases = {
      "Leiam cap. 7",
      "Prova 18/11",
      "Seminário 20/11",
      "Prova Cálculo",
      "Trabalho lógica",
      "Paralisação amanhã",
      "Estudem pág. 45",
      "Seminário adiado",
      "Ponto facultativo",
      "Revisem conceitos",
      "Prova Física",
      "Seminário individual",
      "Trabalho dupla",
      "Revisão quinta",
      "Entrega trabalhos",
      "Prova surpresa",
    };
    Random random = new Random();
    int indiceSorteado = random.nextInt(frases.length);
    return frases[indiceSorteado];
  }
}

