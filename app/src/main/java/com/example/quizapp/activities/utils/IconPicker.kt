package com.example.quizapp.activities.utils

import com.example.quizapp.R

object IconPicker {
    val Icons = arrayOf(
        R.drawable._0042978_education_bag_learning_study_school_icon,
        R.drawable._15795_study_icon,
        R.drawable._771600_book_education_learning_ruler_school_icon,
        R.drawable._213590_bell_doodle_education_line_school_icon,
        R.drawable._064476_education_learn_pencil_student_study_icon,
        R.drawable._295011_school_education_university_learning_study_icon,
        R.drawable._00473_component_concept_modeling_problem_puzzle_icon,
    )
    var currentIcon = 0

    fun getIcons(): Int{
        currentIcon = (currentIcon + 1) % Icons.size
        return Icons[currentIcon]
    }

}