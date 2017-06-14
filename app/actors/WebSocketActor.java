package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class WebSocketActor extends UntypedAbstractActor {

    private final ActorRef out;

    public WebSocketActor(ActorRef out) {
        this.out = out;
    }
    public static Props props(ActorRef out) {
        return Props.create(WebSocketActor.class, out);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String) {
            out.tell(message, getSelf());
        }
    }
}
