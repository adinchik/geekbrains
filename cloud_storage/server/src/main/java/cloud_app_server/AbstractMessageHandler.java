package cloud_app_server;

import auth.AuthService;
import auth.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.*;

import java.nio.file.Files;

public class AbstractMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private User user;

    public AbstractMessageHandler()
    {
        user = null;
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
                userAuth.setStatus(Server.authService.isUserExist(userAuth));
                ctx.writeAndFlush(userAuth);
                if (userAuth.getStatus()) {
                    user = Server.authService.getUserByLoginAndPassword(userAuth);
                    ctx.writeAndFlush(new FilesList(user.getPath()));
                }
                break;
            case USER_REGISTER:
                UserRegister userRegister = (UserRegister) message;
                user = Server.authService.createNewUser(userRegister);
                ctx.writeAndFlush(userRegister);
                ctx.writeAndFlush(new FilesList(user.getPath()));
                break;
        }
    }
}
