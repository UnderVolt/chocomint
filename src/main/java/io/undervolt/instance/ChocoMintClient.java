package io.undervolt.instance;

public class ChocoMintClient {
    
    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:
                //TODO: Register events & hooks
                break;
            case POSTINIT:
                //TODO: Throw post setup
                break;            
        }
    }

}
