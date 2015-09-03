package com.three_stack.digital_compass.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class App 
{
    public static void main( String[] args ) throws URISyntaxException, MalformedURLException
    {
    	final Socket socket = IO.socket("http://192.168.0.109:3000");
    	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

    	  //@Override
    	  public void call(Object... args) {
    	    socket.emit("foo", "hi");
    	    socket.disconnect();
    	    System.out.println("connect");
    	  }

    	}).on("event", new Emitter.Listener() {

    	  //@Override
    	  public void call(Object... args) {
    		  System.out.println("event");
    	  }

    	}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

    	  //@Override
    	  public void call(Object... args) {
    		  System.out.println("dc");
    	  }

    	}).on("boop", new Emitter.Listener() {

      	  //@Override
      	  public void call(Object... args) { 
      		  System.out.println ("boop");
      	  }

      	});
    	socket.connect();
    	while(true) {}
    }
}
