package ETU.web_lab_8.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import ETU.web_lab_8.shared.Student;

/**
 * Клиентская часть: точка входа приложения.
 * Пользователь вводит фамилию ученика и получает его класс и сведения о присутствии в школе.
 */
public class StudentsModule implements EntryPoint {

    // Прокси для вызова удаленного сервиса на сервере
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    private static final String SRV_ERR = "Ошибка сервера! ";
    private static final String EMPTY_SURNAME = "Введите фамилию ученика.";
    private static final String FIND_BTN = "Найти";
    private static final String CLOSE_BTN = "Закрыть";
    private static final String INFO_WND_TITLE = "Информация об ученике: ";

    public void onModuleLoad() {
        final TextBox surnameBox = new TextBox();
        final Label errorLabel = new Label();
        final Button sendButton = new Button(FIND_BTN);
        sendButton.addStyleName("sendButton");

        // Добавление элементов в контейнеры HTML-страницы
        RootPanel.get("surnameBoxContainer").add(surnameBox);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("sendButtonContainer").add(sendButton);
        surnameBox.setFocus(true);

        // Диалоговое окно с таблицей результата
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText(INFO_WND_TITLE);
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button(CLOSE_BTN);
        closeButton.getElement().setId("closeButton");

        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");

        final CellTable<Student> table = createCellTable();
        dialogVPanel.add(table);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                sendButton.setEnabled(true);
                surnameBox.setFocus(true);
                surnameBox.selectAll();
            }
        });

        // Обработчик кнопки "Найти" и клавиши Enter
        class RPCClickHandler implements ClickHandler, KeyUpHandler {
            public void onClick(ClickEvent event) {
                sendSurnameToServer();
            }

            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sendSurnameToServer();
                }
            }

            private void sendSurnameToServer() {
                errorLabel.setText("");
                final String surname = surnameBox.getText().trim();
                if (surname.isEmpty()) {
                    errorLabel.setText(EMPTY_SURNAME);
                    return;
                }
                sendButton.setEnabled(false);
                greetingService.findBySurname(surname, new AsyncCallback<List<Student>>() {
                    public void onFailure(Throwable caught) {
                        // Сообщение сервера (например, "Ученик с фамилией ... не найден")
                        errorLabel.setText(SRV_ERR + caught.getMessage());
                        sendButton.setEnabled(true);
                    }

                    public void onSuccess(List<Student> result) {
                        dialogBox.setText(INFO_WND_TITLE + surname);
                        table.setRowCount(result.size(), true);
                        table.setRowData(0, result);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }
                });
            }
        }

        RPCClickHandler handler = new RPCClickHandler();
        sendButton.addClickHandler(handler);
        surnameBox.addKeyUpHandler(handler);
    }

    // Таблица: ФИО, Класс, Присутствует в школе, Причина отсутствия
    private CellTable<Student> createCellTable() {
        final CellTable<Student> table = new CellTable<Student>();
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        TextColumn<Student> fioColumn = new TextColumn<Student>() {
            public String getValue(Student object) {
                return object.getFio();
            }
        };
        table.addColumn(fioColumn, "ФИО ученика");

        TextColumn<Student> classColumn = new TextColumn<Student>() {
            public String getValue(Student object) {
                return object.getSchoolClass();
            }
        };
        table.addColumn(classColumn, "Класс");

        TextColumn<Student> presentColumn = new TextColumn<Student>() {
            public String getValue(Student object) {
                return object.isPresent() ? "Да" : "Нет";
            }
        };
        table.addColumn(presentColumn, "Присутствует в школе");

        TextColumn<Student> reasonColumn = new TextColumn<Student>() {
            public String getValue(Student object) {
                return object.getReason();
            }
        };
        table.addColumn(reasonColumn, "Причина отсутствия");

        return table;
    }
}