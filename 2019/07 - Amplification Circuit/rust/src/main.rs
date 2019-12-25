use std::io;
use std::io::prelude::*;
use std::fs::File;
use std::collections::HashSet;

struct BestAnswer {
    value: i32,
}

fn get_value(mode: i32, curr_value: i32, program: &mut Vec<i32>) -> i32 {
    if mode == 0 {
        return program[curr_value as usize];
    } else {
        return curr_value;
    }
}

fn execute_program(program: &mut Vec<i32>, insert_val: Vec<i32>) -> i32 {
    let mut curr_index = 0;
    let mut insert_index = 0;
    while program[curr_index] != 99 {
        let curr_instruction = program[curr_index];
        let opcode = curr_instruction % 10;
        let first_mode = (curr_instruction / 100) % 10;
        let first_val = get_value(first_mode, program[(curr_index + 1) as usize], program);
        match opcode {
            1 | 2 => {
                let second_mode = (curr_instruction / 1000) % 10;
                let second_val = get_value(second_mode, program[(curr_index + 2) as usize], program);
                let store_index = program[curr_index + 3] as usize;
                if opcode == 1 {
                    program[store_index] = first_val + second_val;
                } else {
                    program[store_index] = first_val * second_val;
                }
                curr_index += 4;
            },
            3 | 4 => {
                let pos_index = program[curr_index + 1] as usize;
                if opcode == 3 {
                    let inserting = insert_val[insert_index  as usize];
                    program[pos_index] = inserting;
                    insert_index = insert_index + 1;
                } else {
                    return get_value(first_mode,  program[curr_index + 1], program);
                }
                curr_index += 2;
            },
            5 | 6 => {
                let second_mode = (curr_instruction / 1000) % 10;
                let second_val = get_value(second_mode, program[(curr_index + 2) as usize], program) as usize;
                if opcode == 5 && first_val != 0 {
                    curr_index = second_val;
                } else if opcode == 6 && first_val == 0 {
                    curr_index = second_val;
                } else {
                    curr_index += 3;
                }
            },
            7 | 8 => {
                let second_mode = (curr_instruction / 1000) % 10;
                let second_val = get_value(second_mode, program[(curr_index + 2) as usize], program);
                let mut to_store = 0;
                if opcode == 7 && first_val < second_val  {
                    to_store = 1;
                } else if opcode == 8 && first_val == second_val {
                    to_store = 1;
                }
                let store_index = program[curr_index + 3] as usize;
                program[store_index] = to_store;
                curr_index += 4;
            }
            _ => println!("Not supposed to be here!")
        }
    }
    return program[0];
}

fn execute_sequence(program: &mut Vec<i32>, input_values: Vec<i32>, answer: &mut BestAnswer) {
    let mut prev_output = 0;
    for i in input_values {
        let mut cloned = program.clone();
        prev_output = execute_program(&mut cloned, vec![i, prev_output]);
        print!("{} ", i);
    }
    println!("{}", prev_output);
    if prev_output > answer.value {
        answer.value = prev_output;
    }
}

fn helper(program: &mut Vec<i32>, used: &mut HashSet<i32>, input_values: &mut Vec<i32>, answer: &mut BestAnswer) {
    if input_values.len() == 5 {
        execute_sequence(program, input_values.to_vec(), answer);
    } else {
        for i in 0..5 {
            if !used.contains(&i) {
                input_values.push(i);
                used.insert(i);
                helper(program, used, input_values, answer);
                input_values.pop();
                used.remove(&i);
            }
        }
    }
}

fn get_max_amplication(program: &mut Vec<i32>) -> i32 {
    let mut used: HashSet<i32> = HashSet::new();
    let mut best_answer = BestAnswer {
        value: 0,
    };
    let mut input_values: Vec<i32> = vec![];
    helper(program, &mut used, &mut input_values, &mut best_answer);
    return best_answer.value;
}

fn main() -> io::Result<()> {
    let mut f = File::open("../input")?;
    let mut buffer = String::new();

    f.read_to_string(&mut buffer)?;

    let trimmed = buffer.trim();

    let program: Vec<i32> = trimmed.split(",").map(|s| s.parse().unwrap()).collect();

    println!("===== Part 1 ======");
    let mut program1 = program.clone();
    let best_amp = get_max_amplication(&mut program1);
    println!("Part 1: {}", best_amp);

    Ok(())
}

