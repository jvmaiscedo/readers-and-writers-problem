/* ***************************************************************
* Autor............: Joao Victor Gomes Macedo
* Matricula........: 202210166
* Inicio...........: 09/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Principal
* Funcao...........: Este codigo tem a funcao de fazer com que
                    o GUI feito no FXMLDocument seja executado.
*************************************************************** */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.MainController;
import javafx.stage.WindowEvent;
public class Principal extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLDocument.fxml"));//define o caminho e carrega o arquivo FXML
    Scene scene = new Scene(root);//cria a cena
    stage.setScene(scene);//seta a cena
    stage.setResizable(false);//impede o redimensionamento da tela
    stage.show();//exibe a tela
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {//define a finalizacao dos processos ao clicar no icone de fechar a janela.
      @Override
      public void handle(WindowEvent event) {
        Platform.exit();
        System.exit(0);
      }
    });
    }
  public static void main(String[] args) {
    launch(args);//carrega os argumentos
  }
}
