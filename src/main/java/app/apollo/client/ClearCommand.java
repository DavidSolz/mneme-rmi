package app.apollo.client;

public class ClearCommand implements Command{

    @Override
    public void execute(String[] args) throws Exception {
        
        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clear - Clears console";
    }
    
    

}
