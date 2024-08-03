package com.mycompany.services;

import com.mycompany.models.Tarea;

import java.util.List;

public interface ITareaServicio {
    List<Tarea> listarTareas();

    void guardarTarea(Tarea tarea);

    void eliminarTarea(Tarea tarea);
}
