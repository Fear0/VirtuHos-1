package definitions;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class Text {
    public static final String TITLE = "VirtuHoS";
    public static final String ROOM = "Raum";
    public static final String OFFICE = "Büro";
    public static final String MEETING_ROOM = "Konferenzraum";
    public static final String HALL = "Halle";

    public static final String YES = "Ja";
    public static final String NO = "Nein";
    public static final String OK = "OK";
    public static final String CANCEL = "Abbrechen";

    public static final ButtonType buttonTypeYes = new ButtonType(Text.YES, ButtonBar.ButtonData.YES);
    public static final ButtonType buttonTypeNo = new ButtonType(Text.NO, ButtonBar.ButtonData.NO);
    public static final ButtonType buttonTypeOk = new ButtonType(Text.OK, ButtonBar.ButtonData.OK_DONE);
    public static final ButtonType buttonTypeCancel = new ButtonType(Text.CANCEL, ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final String BUILDING_NO_HALL = "Es gibt keine Halle.";
    public static final String BUILDING_MULTIPLE_HALLS = "Es gibt mehr als eine Halle.";
    public static final String BUILDING_ROOMS_DO_NOT_TOUCH = "Einige Räume berühren sich nicht.";
    public static final String BUILDING_LEGAL = "legal";

    public static final String DATABASE_CONNECTION_NO_CONNECTION = "Es konnte keine Verbindung zur Datenbank hergestellt werden.";
    public static final String DATABASE_CONNECTION_NAME_DIALOG = "Unter welchem Namen soll das Gebäude gespeichert werden?";
    public static final String DATABASE_CONNECTION_NAME = "Name:";
    public static final String DATABASE_CONNECTION_DUPLICATE_NAME_1 = "Es ist bereits ein Gebäude mit diesem Namen gespeichert. Soll es überschrieben werden?";
    public static final String DATABASE_CONNECTION_DUPLICATE_NAME_2 = "Es wurde bereits ein Gebäude mit diesem Namen von %s am %s gespeichert. Soll es überschrieben werden?";
    public static final String DATABASE_CONNECTION_NULL = "[null]";
    public static final String DATABASE_CONNECTION_LOAD_EXCEPTION = "Das Gebäude konnte nicht geladen werden. Soll es gelöscht werden?";
    public static final String DATABASE_CONNECTION_SELECTION_DIALOG = "Welches Gebäude soll geladen werden?";
    public static final String DATABASE_CONNECTION_NO_BUILDINGS = "Es gibt keine Gebäude.";


    public static final String EDITOR_CONTROLLER_LEAVE_DIALOG = "Wollen Sie den Editor wirklich verlassen?";
    public static final String EDITOR_CONTROLLER_SAVE_WARNING = "%s Wollen Sie das Gebäude wirklich speichern?";
    public static final String EDITOR_CONTROLLER_CHAIRS_IN_HALL = "In einer Halle dürfen keine Stühle platziert werden!";
    public static final String EDITOR_CONTROLLER_LINK_1 = "link";
    public static final String EDITOR_CONTROLLER_LINK_2 = "Link:";
    public static final String EDITOR_CONTROLLER_LINK_DIALOG = "Bitte geben Sie einen Link zu ihrem Dokument an.";
    public static final String EDITOR_CONTROLLER_LINK_NEW = "Bitte geben Sie einen neuen Link an.";
    public static final String EDITOR_CONTROLLER_ROOM_SIZE_ERROR = "Ein Raum muss mindestens 10 Felder groß sein.";
    public static final String EDITOR_CONTROLLER_DELETE_DIALOG = "Wollen Sie diesen Raum und allen Inhalt löschen?";
    public static final String EDITOR_CONTROLLER_CHAIRS_IN_HALL_DIALOG = "In diesem Raum stehen Stühle. Wollen Sie die Stühle löschen, um den Raum zu einer Halle zu machen?";
    public static final String EDITOR_CONTROLLER_NAME = "Name:";
    public static final String EDITOR_CONTROLLER_NAME_DIALOG_1 = "Welchen Namen soll der Raum haben?";
    public static final String EDITOR_CONTROLLER_NAME_DIALOG_2 = "Bitte geben Sie einen noch nicht verwendeten Namen ein.";

    public static final String MAIN_MENU_CONTROLLER_NO_USERNAME = "Sie sind nicht eingeloggt. Bitte loggen sie sich ein.";

    public static final String SELECTION_CONTROLLER_CHAIRS_IN_HALL = "In einer Halle dürfen keine Stühle platziert werden!";

    public static final String SHOW_CONTROLLER_LINK_ERROR = "Das Dokument konnte nicht geöffnet werden. Ist der Link korrekt angegeben?";
    public static final String SHOW_CONTROLLER_ROOM_LOCKED = "Dieser Raum ist abgeschlossen. Wollen sie ihn trotzdem betreten?";
    public static final String SHOW_CONTROLLER_NO_FREE_CHAIR = "In diesem Raum gibt es keinen freien Stuhl. Bitte wählen SIe einen anderen Raum aus oder warten Sie bis ein Platz frei wird.";
    public static final String SHOW_CONTROLLER_CHAIR_NOT_FREE = "Dieser Stuhl ist besetzt.";
}
