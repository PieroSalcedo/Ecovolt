package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalog")
public class Catalog{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalog")
    private Integer idCatalog;

    /*
     * REGLA DE NEGOCIO: Definición de Categoría Maestra.
     * Almacena el nombre del grupo de opciones.
     * Ejemplos: 'TIPO_HABITACION', 'CATEGORIA_DISPOSITIVO', 'ESTADO_EQUIPO'.
     * Sirve para agrupar dinámicamente todos los valores que el usuario verá en los combos.
     */
    @Column(name = "description", nullable = false, length = 100)
    private String description;

}