package cloud_app_server;

import auth.AuthService;
import auth.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AbstractMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private User user;
    private AuthService authService;

    public AbstractMessageHandler()
    {
        user = null;
        authService = new AuthService();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.log.debug("Client connected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                AbstractMessage message) throws Exception {
        Server.log.debug("received: {}", message);
        switch (message.getMessageType()) {
            case FILE_REQUEST:
                FileRequest req = (FileRequest) message;
                ctx.writeAndFlush(
                        new FileMessage(user.getPath().resolve(req.getFileName()))
                );
                break;
            case FILE:
                FileMessage fileMessage = (FileMessage) message;
                Files.write(
                        user.getPath().resolve(fileMessage.getFileName()),
                        fileMessage.getBytes()
                );
                ctx.writeAndFlush(new FilesList(user.getPath()));
                break;
            case USER_AUTH:
                UserAuth userAuth = (UserAuth) message;
                userAuth.setStatus(authService.isUserExist(userAuth));
                ctx.writeAndFlush(userAuth);
                if (userAuth.getStatus()) {
                    ctx.writeAndFlush(new FilesList(user.getPath()));
                    user = authService.getUserByLoginAndPassword(userAuth);
                }
            case USER_REGISTER:
                UserRegister userRegister = (UserRegister) message;
                user = authService.createNewUser(userRegister);
                ctx.writeAndFlush(userRegister);
                ctx.writeAndFlush(new FilesList(user.getPath()));

        }
    }
}
