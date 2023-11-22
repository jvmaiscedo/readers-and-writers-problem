/* ***************************************************************
* Autor............: Joao Victor Gomes Macedo
* Matricula........: 202210166
* Inicio...........: 09/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Reader
* Funcao...........: Modela o comportamento dos leitores.
*************************************************************** */
package model;
import controller.MainController;
import javafx.application.Platform;
public class Reader extends Thread{
  private int id;//id dos leitores
  private MainController control;//instancia do controle para manipulacao de imagens
  private String text;//texto que sera apresentado no label de cada um
  private volatile boolean isPaused = false;//flag para pausar o processo
  
  /* ***************************************************************
  * Metodo: Reader
  * Funcao: Construtor do objeto Reader.
  * Parametros: Id do leitor e o controlador da interface.
  * Retorno: Sem retorno.
  *************************************************************** */
  public Reader(int n, MainController control){
    this.id = n;
    this.control = control;
  }
  
  /* ***************************************************************
  * Metodo: run
  * Funcao: Executar o ciclo de vida do leitor.
  * Parametros: Sem parametros.
  * Retorno: Sem retorno.
  *************************************************************** */
  @Override
  public void run() {
    while (!Thread.interrupted()){//se a thread for interrompida, interrompe o loop.
      try {
        MainController.mutex.acquire();//obtem acesso exclusivo a ‘rc’
        MainController.rc+=1;//um leitor a mais agora
        if(MainController.rc == 1){//se este for o primeiro leitor, adquire acesso a db.
          MainController.db.acquire();
        }
        MainController.mutex.release();//libera o acesso exclusivo a ‘rc’
        readDataBase();//acesso aos dados
        MainController.mutex.acquire();//obtem acesso exclusivo a ‘rc’
        MainController.rc-=1;//um leitor a menos agora
        if(MainController.rc == 0){//se este for o ultimo leitor libera o acesso a db.
          MainController.db.release();
        }
        MainController.mutex.release();//libera o acesso exclusivo a ‘rc’
        useDataRead();//regiao nao critica
      } catch (InterruptedException e) {}
    }
  }

  /* ***************************************************************
  * Metodo: useDataRead
  * Funcao: Realiza o estágio de uso do dado lido pelo estudante.
  * Parametros: Sem parametros.
  * Retorno: Sem retorno.
  *************************************************************** */
  private void useDataRead() {
    Platform.runLater(()-> control.changeStudentUdr(this.id));//altera a imagem
    Platform.runLater(()-> control.setDrTextStudent(this.id, this.text));//seta o texto do label para o dado lido na base de dados
    sleepTime(control.usingDr(this.id));//processo dorme por um determinado tempo
    pausando();//caso seja pausado,o processo entrara em espera ocupada mantendo o seu estado atual
    Platform.runLater(()-> control.setDrTextStudent(this.id,""));//seta o texto do label para vazio
    Platform.runLater(()-> control.changeStudent(this.id));//altera a imagem
  }
  
  /* ***************************************************************
  * Metodo: readDataBase
  * Funcao: Realiza o estágio de leitura da base de dados.
  * Parametros: Sem parametros.
  * Retorno: Sem retorno.
  *************************************************************** */
  private void readDataBase() {
    Platform.runLater(()-> control.changeStudentRead(this.id));//altera a imagem
    text = control.getDataBaseText();//atribui ao atributo texto o valor lido na base de dados.
    sleepTime(control.readingData(this.id));//processo dorme por um determinado tempo
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
}
