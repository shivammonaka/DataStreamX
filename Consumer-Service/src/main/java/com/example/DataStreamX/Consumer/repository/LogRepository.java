package com.example.DataStreamX.Consumer.repository;

import com.example.DataStreamX.Consumer.model.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEvent, Long> {}