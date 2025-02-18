package com.himiko;



import com.himiko.game.utils.UserData;
import com.himiko.logger.Logger;
import com.himiko.logger.LoggerBuilder;
import com.himiko.network.NetworkWrapper;
import com.himiko.network.protocol.enums.PackageCategory;

/**
 * @author Valk on 14.02.2025
 * @project quizClient
 */
public class Main {
    public static Logger logger = LoggerBuilder.build("logs.txt").getLogger();
    public static NetworkWrapper networkWrapper;

    public static void main(String[] args) {
        try {
            networkWrapper = new NetworkWrapper();
            networkWrapper.start();
            networkWrapper.getPackageHandler().<UserData>sendData(new UserData("test"), PackageCategory.USER_DATA);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
