package com.cchqsa.job_application_tracker.mapper;


public interface ModelMapper<A, B>{
    B mapTo(A a);
    A mapFrom(B b);
}
