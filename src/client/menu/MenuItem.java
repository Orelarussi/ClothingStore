package client.menu;

public class MenuItem {

    private final String title;
    private final MenuAction action;

    public MenuItem(String title, MenuAction action) {
        this.title = title;
        this.action = action;
    }

    public void run() {
        action.run();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
