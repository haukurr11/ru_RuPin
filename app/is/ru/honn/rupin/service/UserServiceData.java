package is.ru.honn.rupin.service;

/**
 * Created with IntelliJ IDEA.
 * Created by: Haukur Rósinkranz, Knútur Óli Magnússon
 * 13.11.2012 - 20.11.2012
 */

import is.ru.honn.rupin.data.UserDataGateway;
import is.ru.honn.rupin.domain.User;
import is.ruframework.data.RuDuplicateDataException;

import java.util.List;

public class UserServiceData implements UserService
{
    private UserDataGateway userDataGateway;

    @Override
    public User signup(User user) throws UsernameExistsException
    {
        try
            {
                userDataGateway.add(user);
            }
        catch (RuDuplicateDataException ddex)
            {
                throw new UsernameExistsException();
            }
        return user;
    }

    @Override
    public User login(String username, String password)
    {
        User user = this.userDataGateway.getUserByUsername(username);
        if (user == null)
            return null;
        if (!user.getPassword().equals(password))
            return null;
        return user;
    }

    @Override
    public User getUser(String username)
    {
        return this.userDataGateway.getUserByUsername(username);
    }

    @Override
    public List<User> getFollowers(String username)
    {
        return userDataGateway.getFollowersOf(username);
    }

    @Override
    public List<User> getUsersFollowedBy(String username)
    {
        return userDataGateway.getUsersFollowedBy(username);
    }

    public void setUserDataGateway(UserDataGateway userDataGateway)
    {
        this.userDataGateway = userDataGateway;
    }
    public void AddFollower(String username, String following)
    {
        userDataGateway.addFollower(username, following);
    }
    public void StopFollowing(String username, String following)
    {
        userDataGateway.stopFollowing(username, following);
    }
}
