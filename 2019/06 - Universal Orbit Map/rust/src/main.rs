use std::fs::File;
use std::io::{BufReader, BufRead, Error};
use std::collections::HashMap;
use std::collections::HashSet;
use std::collections::VecDeque;

fn bfs1(edges: HashMap<String, Vec<String>>) -> i32 {
    let mut queue = VecDeque::new();
    let mut touched = HashSet::new();
    queue.push_back("COM");
    touched.insert("COM");

    let mut num_orbits = 0;

    let mut iteration = 0;

    while queue.len() > 0 {
        let curr_size = queue.len() as i32;
        num_orbits += curr_size * iteration;

        for _ in 0..curr_size {
            match queue.pop_front() {
                Some(curr_station) => {
                    match edges.get(curr_station) {
                        Some(connected_stations) => {
                            for station in connected_stations {
                                if !touched.contains(station.as_str()) {
                                    queue.push_back(station);
                                    touched.insert(station);
                                }
                            }
                        },
                        None => ()
                    }
                },
                None => println!("Yo we dead"),
            }
        }
        iteration += 1;
    }
    return num_orbits;
}

fn bfs2(edges: HashMap<String, Vec<String>>) -> i32 {
    let mut queue = VecDeque::new();
    let mut touched = HashSet::new();
    queue.push_back("YOU");
    touched.insert("YOU");

    let mut iteration = 0;

    while queue.len() > 0 {
        let curr_size = queue.len() as i32;

        for _ in 0..curr_size {
            match queue.pop_front() {
                Some(curr_station) => {
                    match edges.get(curr_station) {
                        Some(connected_stations) => {
                            for station in connected_stations {
                                if !touched.contains(station.as_str()) {
                                    if station == "SAN" {
                                        return iteration - 1;
                                    }
                                    queue.push_back(station);
                                    touched.insert(station);
                                }
                            }
                        },
                        None => ()
                    }
                },
                None => println!("Yo we dead"),
            }
        }
        iteration += 1;
    }
    return -1;
}

fn main() -> Result<(), Error> {
    let path = "../input";

    let input = File::open(path)?;
    let buffered = BufReader::new(input);

    let mut edges = HashMap::new();

    // create graph
    for line in buffered.lines() {
        let line_str = line.unwrap();
        let parsed: Vec<&str> = line_str.split(")").collect();
        let edge1 = parsed[0].to_string();
        let edge2 = parsed[1].to_string();
        edges.entry(edge1.clone()).or_insert(vec![]).push(edge2.clone());
        edges.entry(edge2.clone()).or_insert(vec![]).push(edge1.clone());
    }
    // bfs

    println!("===== Part 1 =====");
    println!("{}", bfs1(edges.clone()));
    println!("===== Part 2 =====");
    println!("{}", bfs2(edges.clone()));
    Ok(())
}