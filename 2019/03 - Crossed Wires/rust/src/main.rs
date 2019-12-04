use std::fs::File;
use std::io::{BufReader, BufRead, Error};
use std::collections::HashSet;
use std::cmp;
use std::collections::HashMap;

enum Direction {
    R,
    D,
    L,
    U,
    E,
}

struct Move {
    direction: Direction,
    magnitude: i32,
}

#[derive(Hash, Eq, PartialEq, Debug)]
struct Coord {
    x: i32,
    y: i32,
}

fn generate_move(move_str: &str) -> Move {
    let d_str = &move_str[..1];
    let d = match d_str {
        "R" => Direction::R,
        "D" => Direction::D,
        "L" => Direction::L,
        "U" => Direction::U,
        _ => Direction::E,
    };
    return Move {
        direction: d,
        magnitude: move_str[1..].parse().unwrap()
    };
}

fn get_coords(moves: &Vec<Move>) -> (HashSet<Coord>, HashMap<Coord, i32>) {
    let mut x = 0;
    let mut y = 0;
    let mut coords = HashSet::new();
    let mut dist_map = HashMap::new();
    let mut dist = 0;
    for m in moves {
        let dy = match m.direction {
            Direction::U => -1,
            Direction::D => 1,
            _ => 0
        };
        let dx = match m.direction {
            Direction::L => -1,
            Direction::R => 1,
            _ => 0
        };
        for _ in 0..m.magnitude {
            x += dx;
            y += dy;
            coords.insert(Coord {
                x: x,
                y: y,
            });
            dist += 1;
            dist_map.insert(Coord {
                x: x,
                y: y,
            }, dist);
        }
    }
    return (coords, dist_map);
}

fn generate_moves(moves_str: &str) -> Vec<Move> {
    let mut moves: Vec<Move> = vec![];
    for split in moves_str.split(",") {
        moves.push(generate_move(split));
    }
    return moves;
}

fn main() -> Result<(), Error> {
    let path = "../input";
    let input = File::open(path)?;
    let buffered = BufReader::new(input);

    let mut moves: Vec<Vec<Move>> = vec![];
    for line in buffered.lines() {
        let parsed_line = line.unwrap();
        let trimmed = parsed_line.trim();
        moves.push(generate_moves(trimmed));
    }

    let (coords1, dist1) = get_coords(&moves[0]);
    let (coords2, dist2) = get_coords(&moves[1]);

    let mut best_manhattan_distance = std::i32::MAX;
    for intersect in coords1.intersection(&coords2) {
        let dist = intersect.x.abs() + intersect.y.abs();
        best_manhattan_distance = cmp::min(dist, best_manhattan_distance);
    }

    println!("===== Part 1 =====");
    println!("{}", best_manhattan_distance);

    let mut best_step_distance = std::i32::MAX;
    for intersect in coords1.intersection(&coords2) {
        let step1 = match dist1.get(intersect) {
            Some(step) => step,
            None => &0,
        };
        let step2 = match dist2.get(intersect) {
            Some(step) => step,
            None => &0,
        };
        let dist = step1 + step2;
        best_step_distance = cmp::min(dist, best_step_distance);
    }

    println!("===== Part 2 =====");
    println!("{}", best_step_distance);

    Ok(())
}
