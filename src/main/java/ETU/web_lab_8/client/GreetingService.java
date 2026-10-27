package ETU.web_lab_8.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ETU.web_lab_8.shared.Student;

/**
 * Удаленный интерфейс сервиса (RPC). Реализуется на сервере в GreetingServiceImpl.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    /**
     * Поиск учеников по фамилии.
     *
     * @param surname фамилия ученика
     * @return список найденных учеников (класс, присутствие, причина отсутствия)
     * @throws IllegalArgumentException если фамилия пустая или ученик не найден
     */
    List<Student> findBySurname(String surname) throws IllegalArgumentException;
}