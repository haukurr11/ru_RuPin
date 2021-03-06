package controllers;

import is.ru.honn.rupin.domain.Board;
import is.ru.honn.rupin.domain.Pin;
import is.ru.honn.rupin.domain.User;
import is.ru.honn.rupin.service.PinService;
import play.mvc.Result;
import viewmodels.IndexModel;
import views.html.index;

import java.util.ArrayList;
import java.util.List;


public class Application extends RuPinController
{
private static PinService pinService = (PinService)ctx.getBean("pinService");

    public static Result index()
    {
        String loggedInUsername = session().get("username");
        if(loggedInUsername == null)
            return redirect( routes.Session.login()  );
        IndexModel current = new IndexModel();
        current.setUser(userService.getUser(loggedInUsername));
        List<Pin> followedPins = new ArrayList<>();
        for(User user : userService.getUsersFollowedBy(loggedInUsername))
            for(Board board : pinService.getBoards(user.getUsername() ))
                {
                    board.setCreator(userService.getUser(user.getUsername()));
                    followedPins.addAll(board.getPins());
                }
        current.setPins(followedPins);
        return ok(index.render(current));
    }
    public static Result user(String username)
    {
        String loggedInUsername = session().get("username");
        if(loggedInUsername == null)
            return redirect( routes.Session.login()  );
        User user = userService.getUser(username) ;
        return ok(views.html.user.render(user));
    }

}