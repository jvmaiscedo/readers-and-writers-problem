/* ***************************************************************
* Autor............: Joao Victor Gomes Macedo
* Matricula........: 202210166
* Inicio...........: 09/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: MainController
* Funcao...........: Manipula os objetos da interface gráfica 
		                 e das classes modelo.
*************************************************************** */
package controller;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import model.Reader;
import model.Writer;
public class MainController implements Initializable {
  //Elementos de imagem
  @FXML
  ImageView student1back;
  @FXML
  ImageView student1front;
  @FXML
  ImageView student1udr;
  @FXML
  ImageView student2back;
  @FXML
  ImageView student2front;
  @FXML
  ImageView student2udr;
  @FXML
  ImageView student3back;
  @FXML
  ImageView student3front;
  @FXML
  ImageView student3udr;
  @FXML
  ImageView student4back;
  @FXML
  ImageView student4front;
  @FXML
  ImageView student4udr;
  @FXML
  ImageView student5back;
  @FXML
  ImageView student5front;
  @FXML
  ImageView student5udr;
  @FXML
  ImageView professor1front;
  @FXML
  ImageView professor1back;
  @FXML
  ImageView professor2front;
  @FXML
  ImageView professor2back;
  @FXML
  ImageView professor3front;
  @FXML
  ImageView professor3back;
  @FXML
  ImageView professor4front;
  @FXML
  ImageView professor4back;
  @FXML
  ImageView professor5front;
  @FXML
  ImageView professor5back;
  //Elementos utilizados para ilustrar os dados
  @FXML
  Label dataBaseText;
  @FXML
  Label drTextStudent1;
  @FXML
  Label drTextStudent2;
  @FXML
  Label drTextStudent3;
  @FXML
  Label drTextStudent4;
  @FXML
  Label drTextStudent5;
  //Elementos de controle
  //professor 1
  @FXML
  Slider thinkingData1;
  @FXML
  Slider writingData1;
  //professor 2
  @FXML
  Slider thinkingData2;
  @FXML
  Slider writingData2;
  //professor 3
  @FXML
  Slider thinkingData3;
  @FXML
  Slider writingData3;
  //professor 4
  @FXML
  Slider thinkingData4;
  @FXML
  Slider writingData4;
  //professor 5
  @FXML
  Slider thinkingData5;
  @FXML
  Slider writingData5;
  //estudante 1
  @FXML
  Slider usingDr1;
  @FXML
  Slider readingData1;
  //estudante 2
  @FXML
  Slider usingDr2;
  @FXML
  Slider readingData2;
  //estudante 3
  @FXML
  Slider usingDr3;
  @FXML
  Slider readingData3;
  //estudante 4
  @FXML
  Slider usingDr4;
  @FXML
  Slider readingData4;
  //estudante 5
  @FXML
  Slider usingDr5;
  @FXML
  Slider readingData5;
  //semaforos e variaveis de controle
  public static Semaphore mutex;//mutex para a mudanca da variavel rc
  public volatile static int rc;//variavel para contar o numero de estudantes na sala de avisos
  public static Semaphore db;//semaforo para o acesso a sala de avisos.
  public Reader [] estudante = new Reader[5];//vetor para guardar as instancias dos objetos estudante
  public Writer [] professor = new Writer[5];//vetor para guardar as instancias dos objetos professor
  //vetor para guardar imagens que se agrupam por funcao e melhorar a manipulacao das imagens
  public ImageView [] professorFront;
  public ImageView [] professorBack;
  public ImageView [] studentUdr;
  public ImageView [] studentBack;
  public ImageView [] studentFront;
  public Label [] drTextStudent;
  //vetor para guardar os sliders e melhorar o acesso aos valores destes.
  public Slider [] thinkingData;
  public Slider [] writingData;
  public Slider [] usingDr;
  public Slider [] readingData;

  /* ***************************************************************
  * Metodo: initialize
  * Funcao: invoca os metodos que instanciam os objetos.
  * Parametros: padrao do java
  * Retorno: sem retorno
  *************************************************************** */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    iniciar();
  }
  
  /* ***************************************************************
  * Metodo: iniciar
  * Funcao: iniciar os elementos, instanciar os objetos professor
  *         estudante, e dar start nas threads.
  * Parametros: Sem parametros
  * Retorno: Sem retorno.
  *************************************************************** */
  public void iniciar(){
    mutex = new Semaphore(1);//instanciando o mutex
    db = new Semaphore(1);//instanciando o db
    rc = 0;//inicializando a variavel rc
    //instanciando e preenchendo os vetores de elementos gráficos.
    studentUdr = new ImageView[]{student1udr, student2udr, student3udr, student4udr, student5udr};
    studentBack = new ImageView[]{student1back, student2back, student3back, student4back, student5back};
    studentFront = new ImageView[]{student1front, student2front, student3front, student4front, student5front};
    drTextStudent = new Label[]{drTextStudent1, drTextStudent2, drTextStudent3, drTextStudent4, drTextStudent5};
    professorFront = new ImageView[]{professor1front, professor2front, professor3front, professor4front, professor5front};
    professorBack = new ImageView[] {professor1back, professor2back, professor3back, professor4back, professor5back};
    thinkingData = new Slider[]{thinkingData1, thinkingData2, thinkingData3, thinkingData4, thinkingData5};
    writingData = new Slider[] {writingData1, writingData2, writingData3, writingData4, writingData5};
    usingDr = new Slider[] {usingDr1, usingDr2, usingDr3, usingDr4, usingDr5};
    readingData = new Slider[] {readingData1, readingData2, readingData3, readingData4, readingData5};
    for (int i=0; i<5; i++){//instanciando as threads e setando valores iniciais.
      estudante[i] = new Reader(i, this);
      professor[i] = new Writer(i, this);
      thinkingData[i].setValue(3);
      writingData[i].setValue(3);
      usingDr[i].setValue(3);
      readingData[i].setValue(3);
    }
    for (int j=0; j<5; j++) {//iniciando a execução das threads
      estudante[j].start();
      professor[j].start();
    }
  }
  
  /* ***************************************************************
  * Metodo: resetar
  * Funcao: Chama as subrotinas que configuram o reset da aplicacao
  * Parametros: Sem parametros
  * Retorno: Sem retorno.
  *************************************************************** */
  @FXML
  public void restart(){
    interromperThreads();
    iniciar();
  }

  /* ***************************************************************
  * Metodo: interromperThreads
  * Funcao: Interrompe as threads.
  * Parametros: Sem parametros
  * Retorno: Sem retorno.
  *************************************************************** */
  public void interromperThreads() {
    for (int j=0; j<5;j++){//pausando as threads para interromper sem haver conflito.
      estudante[j].pausar();
      professor[j].pausar();
    }
    for (int i = 0; i < 5; i++) {//interrompendo as threads e setando as visibilidades iniciais dos elementos graficos.
      estudante[i].interrupt();
      professor[i].interrupt();
      drTextStudent[i].setText("");
      professorFront[i].setVisible(false);
      professorBack[i].setVisible(false);
      studentUdr[i].setVisible(false);
      studentBack[i].setVisible(false);
      studentFront[i].setVisible(false);
    }
    dataBaseText.setText("");
  }
  
  /* ***************************************************************
  * Metodo: thinkingData
  * Funcao: Verifica o valor atual do slider de velocidade no
  *         estagio thinkingData.
  * Parametros: id do professor.
  * Retorno: valor numerico de tipo inteiro.
  *************************************************************** */
  public int thinkingData(int id){
    return (int) thinkingData[id].getValue();
  }
  
  /* ***************************************************************
  * Metodo: writingData
  * Funcao: Verifica o valor atual do slider de velocidade no
  *         estagio writingData.
  * Parametros: id do professor.
  * Retorno: valor numerico de tipo inteiro.
  *************************************************************** */
  public int writingData(int id){
    return (int) writingData[id].getValue();
  }
  
  /* ***************************************************************
  * Metodo: usingDr
  * Funcao: Verifica o valor atual do slider de velocidade no
  *         estagio usingDr.
  * Parametros: id do estudante.
  * Retorno: valor numerico de tipo inteiro.
  *************************************************************** */
  public int usingDr(int id){
    return (int) usingDr[id].getValue();
  }
  
  /* ***************************************************************
  * Metodo: readingData
  * Funcao: Verifica o valor atual do slider de velocidade no
  *         estagio readingData.
  * Parametros: id do estudante.
  * Retorno: valor numerico de tipo inteiro.
  *************************************************************** */
  public int readingData(int id){
    return (int) readingData[id].getValue();
  }
  
  /* ***************************************************************
  * Metodo: changeStudentUdr
  * Funcao: Modifica a visibilidade das imagens do estudante para 
  *	     exemplificar o estado em que usa o dado lido.
  * Parametros: id do estudante.
  * Retorno: sem retorno.
  *************************************************************** */
  public void changeStudentUdr(int id){
    studentUdr[id].setVisible(true);
    studentBack[id].setVisible(false);
    studentFront[id].setVisible(false);
  }
  
  /* ***************************************************************
  * Metodo: changeStudent
  * Funcao: Modifica a visibilidade das imagens do estudante para
  *	     exemplificar o estado em que esta esperando para ler.
  * Parametros: id do estudante.
  * Retorno: sem retorno.
  *************************************************************** */
  public void changeStudent(int id){
    studentUdr[id].setVisible(false);
    studentBack[id].setVisible(false);
    studentFront[id].setVisible(true);
  }
  
  /* ***************************************************************
  * Metodo: changeStudentRead
  * Funcao: Modifica a visibilidade das imagens do estudante para
  *	     exemplificar o estado em que está lendo os informes.
  * Parametros: id do estudante.
  * Retorno: sem retorno
  *************************************************************** */
  public void changeStudentRead(int id){
    studentUdr[id].setVisible(false);
    studentBack[id].setVisible(true);
    studentFront[id].setVisible(false);
  }
  
  /* ***************************************************************
  * Metodo: setDrTextStudent
  * Funcao: Modifica o label referente ao pensamento do estudante 
  *         ao sair da sala de informes.
  * Parametros: id do estudante e texto.
  * Retorno: sem retorno
  *************************************************************** */
  public void setDrTextStudent(int id, String text){
    drTextStudent[id].setText(text);
  }
  
  /* ***************************************************************
  * Metodo: changeProfessorThink
  * Funcao: Modifica a visibilidade das imagens do professor para
  *	     exemplificar o estado em que pensa o dado a ser escrito.
  * Parametros: id do professor.
  * Retorno: sem retorno
  *************************************************************** */
  public void changeProfessorThink(int id){
    professorFront[id].setVisible(true);
    professorBack[id].setVisible(false);
  }
  
  /* ***************************************************************
  * Metodo: changeProfessorWritedb
  * Funcao: Modifica a visibilidade das imagens do professor para
  *	     exemplificar o estado em que escreve no quadro de avisos.
  * Parametros: id do professor.
  * Retorno: sem retorno
  *************************************************************** */
  public void changeProfessorWritedb(int id){
    professorFront[id].setVisible(false);
    professorBack[id].setVisible(true);
  }

  /* ***************************************************************
  * Metodo: getDataBaseText
  * Funcao: Retornar os dados atuais do label que contém os avisos.
  * Parametros: sem parametros.
  * Retorno: string com os dados do label.
  *************************************************************** */
  public String getDataBaseText() {
    return dataBaseText.getText();
  }
  
  /* ***************************************************************
  * Metodo: setDataBaseText
  * Funcao: Modifica o label referente dos avisos.
  * Parametros: texto a ser escrito no label.
  * Retorno: sem retorno
  *************************************************************** */
  public void setDataBaseText(String text) {
    this.dataBaseText.setText(text);
  }
  
   /* ***************************************************************
   * Metodo: pauseProf1
   * Funcao: Pausa a execução do processo especificado.
   * Parametros: sem parametros.
   * Retorno: sem retorno
   *************************************************************** */
  @FXML
  public void pauseProf1(){
    professor[0].pausar();
  }
  
  /* ***************************************************************
  * Metodo: retomarProf1
  * Funcao: Retoma a execução do processo especificado.
  * Parametros: sem parametros.
  * Retorno: sem retorno
  *************************************************************** */
  @FXML
  public void retomarProf1(){
    professor[0].retomar();
  }
  @FXML
  public void pauseProf2(){
    professor[1].pausar();
  }
  @FXML
  public void retomarProf2(){
    professor[1].retomar();
  }
  @FXML
  public void pauseProf3(){
    professor[2].pausar();
  }
  @FXML
  public void retomarProf3(){
    professor[2].retomar();
  }
  @FXML
  public void pauseProf4(){
    professor[3].pausar();
  }
  @FXML
  public void retomarProf4(){
    professor[3].retomar();
  }
  @FXML
  public void pauseProf5(){
    professor[4].pausar();
  }
  @FXML
  public void retomarProf5(){
    professor[4].retomar();
  }
  @FXML
  public void pauseStudent1(){
    estudante[0].pausar();
  }
  @FXML
  public void retomarStudent1(){
    estudante[0].retomar();
  }
  @FXML
  public void pauseStudent2(){
    estudante[1].pausar();
  }
  @FXML
  public void retomarStudent2(){
    estudante[1].retomar();
  }
  @FXML
  public void pauseStudent3(){
    estudante[2].pausar();
  }
  @FXML
  public void retomarStudent3(){
    estudante[2].retomar();
  }
  @FXML
  public void pauseStudent4(){
    estudante[3].pausar();
  }
  @FXML
  public void retomarStudent4(){
    estudante[3].retomar();
  }
  @FXML
  public void pauseStudent5(){
    estudante[4].pausar();
  }
  @FXML
  public void retomarStudent5(){
    estudante[4].retomar();
  }
}
