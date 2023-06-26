/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pratti.pesquisa.repository;

import com.pratti.pesquisa.model.QuizModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Roger
 */
public interface QuizRepository extends JpaRepository<QuizModel, UUID> {

    boolean existsByDescricao(String descricao);
    
    @Query(value = "SELECT a.resposta, COUNT(*) AS quantidade, q.id, q.nome FROM answers AS a JOIN questions AS q ON q.id = a.id_question JOIN quizzes as quiz on quiz.id = a.id_quiz WHERE q.tipo = 'alternativa' and quiz.id = :X GROUP BY a.resposta;", nativeQuery = true)
    List<Object> buscarRespostas(@Param("X")UUID id);
    
}
