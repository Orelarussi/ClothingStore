package server.command_executors;

import server.services.LoginResult;

public interface IExecute {
    String execute(Integer userId, LoginResult loginResult, String request);
}
