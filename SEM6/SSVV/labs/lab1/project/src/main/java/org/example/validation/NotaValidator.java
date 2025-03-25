package org.example.validation;


import org.example.domain.Nota;
import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotaValidator implements Validator<Nota> {
    private StudentXMLRepo studentFileRepository;
    private TemaXMLRepo temaFileRepository;

    /**
     * Class constructor
     * @param studentFileRepository - repository student
     * @param temaFileRepository - repository tema
     */
    public NotaValidator(StudentXMLRepo studentFileRepository, TemaXMLRepo temaFileRepository) {
        this.studentFileRepository = studentFileRepository;
        this.temaFileRepository = temaFileRepository;
    }

    /**
     * Valideaza o nota
     * @param nota - nota pe care o valideaza
     * @throws ValidationException daca nota nu e valida
     */
    @Override
    public void validate(Nota nota) throws ValidationException {
        Student student = studentFileRepository.findOne(nota.getIdStudent());
        if (student== null){
            throw new ValidationException("Studentul nu exista!");
        }

        Tema tema = temaFileRepository.findOne(nota.getIdTema());
        if(tema == null){
            throw new ValidationException("Tema nu exista!");
        }

        double notaC = nota.getNota();
        if(notaC > 10.00 || notaC < 0.00){
            throw new ValidationException("Valoarea notei nu este corecta!");
        }

        if(nota.getData() == null){
            throw new ValidationException("Formatul datei nu este corect!");
        }
        try{
            String dateString = nota.getData().format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch(Exception exception){
            throw new ValidationException("Formatul datei nu este corect!");
        }
    }
}
