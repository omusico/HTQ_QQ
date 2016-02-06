package hq.king.client;

import hq.king.transport.TransportObject;

public interface MessageListener {
void getMessage(TransportObject msg);
}
