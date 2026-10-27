package ETU.web_lab_8.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ETU.web_lab_8.client.GreetingService;
import ETU.web_lab_8.shared.Student;

/**
 * Серверная часть: реализация удаленного интерфейса GreetingService.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    // "База данных" учеников
    private List<Student> db = null;

    public List<Student> findBySurname(String surname) throws IllegalArgumentException {
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Не задана фамилия ученика");
        }
        if (db == null) {
            initDB();
        }

        // Берем первое слово введенной строки (на случай, если введено ФИО целиком)
        String key = surname.trim().split("\\s+")[0];

        List<Student> result = new ArrayList<Student>();
        for (Student s : db) {
            String studentSurname = s.getFio().split("\\s+")[0];
            if (studentSurname.equalsIgnoreCase(key)) {
                result.add(s);
            }
        }

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Ученик с фамилией " + key + " не найден");
        }
        return result;
    }

    private void initDB() {
        db = new ArrayList<Student>();
        db.add(new Student("Иванов Иван Иванович", "10-А", true, ""));
        db.add(new Student("Петров Петр Петрович", "11-Б", false, "болеет"));
        db.add(new Student("Сидорова Анна Сергеевна", "9-В", true, ""));
        db.add(new Student("Кузнецов Алексей Дмитриевич", "10-А", false, "участвует в олимпиаде"));
    }
}