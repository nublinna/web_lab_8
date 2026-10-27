package ETU.web_lab_8.shared;

import java.io.Serializable;

/**
 * Запись об ученике. Передается между сервером и клиентом через GWT-RPC,
 * поэтому реализует Serializable и имеет конструктор по умолчанию.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fio;
    private String schoolClass;
    private boolean present;
    private String reason;

    // Конструктор по умолчанию (обязателен для GWT-RPC)
    public Student() {
    }

    public Student(String fio, String schoolClass, boolean present, String reason) {
        this.fio = fio;
        this.schoolClass = schoolClass;
        this.present = present;
        this.reason = reason;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}