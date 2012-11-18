package controllers;

import is.ru.honn.rupin.domain.Board;
import is.ru.honn.rupin.domain.Pin;
import is.ru.honn.rupin.service.BoardNotFoundException;
import is.ru.honn.rupin.service.PinService;
import play.data.Form;
import play.mvc.Result;
import viewmodels.CreatePinModel;
import viewmodels.ViewBoardModel;
import views.html.board.*;

import java.util.List;

/**
 * Creator: Haukur Rosinkranz
 */
public class BoardController extends RuPinController{
    protected static PinService pinService = (PinService)ctx.getBean("pinService");
    final static Form<Pin> createPinForm = form(Pin.class);
  public static Result viewBoard(String username,String boardname)
  {
      String loggedInUsername = session().get("username");
      if(loggedInUsername == null)
         return redirect( routes.Session.login()  );
      Board board = pinService.getBoard(username, boardname);
      board.setCreator(userService.getUser(username));
      ViewBoardModel vbm = new ViewBoardModel();
      vbm.setUser(userService.getUser(loggedInUsername));
      vbm.setBoard(board);
      return ok(viewboard.render(vbm));
  }
  public static Result myBoards()
  {
      String loggedInUsername = session().get("username");
      if(loggedInUsername == null)
         return redirect( routes.Session.login()  );
      List<Board> boards = pinService.getBoards(loggedInUsername);
      for(Board b : boards) b.setCreator(userService.getUser(loggedInUsername));
      return ok(myboards.render(boards));
  }


    public static Result submitPin(String boardname,String username)
    {

        Form<Pin> filledForm = createPinForm.bindFromRequest();
        Pin created = filledForm.get();
        try{
        pinService.createPin(username,boardname,filledForm.get().getLink(),filledForm.get().getDescription());
        } catch (BoardNotFoundException b)
        {
            b.printStackTrace();
        }
    return null;
    }

    public static Result createPin(String boardname,String username)
    {
        CreatePinModel cpm = new CreatePinModel();
        cpm.setUser(userService.getUser(username));
        cpm.setBoard(pinService.getBoard(username,boardname));
        cpm.setFilledForm(createPinForm);
        return ok(createpin.render(cpm));
    }
}
