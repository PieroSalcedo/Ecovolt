package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    // Auditoría de Fechas (Formato igual al del profe para el JSON)
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Auditoría de Usuarios (Nombres de columnas del script de Postgres)
    @CreatedBy
    @Column(name = "usuario_registro", length = 50, updatable = false)
    private String usuarioRegistro = "SYSTEM";

    @LastModifiedBy
    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;

}