"use client";
import { distance } from "fastest-levenshtein"
import React, { useState, useRef , useEffect} from "react"
import { InputAdornment, OutlinedInput, Popper, Paper, List, ListItem, ClickAwayListener } from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"
import Link from "next/link";

export default function Searchbar(){

    const [wasteItems, setWasteItems] = useState<string[]>([]);

    useEffect(() => {
        const getWasteItems = async () =>{
            const response = await fetch("http://localhost:9876/api/waste");

            const data: { name: string }[] = await response.json();
            const items = data.map(obj => obj.name)
            console.log("data:",items)
            setWasteItems(items);
        };
        getWasteItems();
    }, []);

    const [query, setQuery] = useState("")
    const itemList = wasteItems;
    const [queryResult, setQueryResult] = useState<string[]>([])
    const [open, setOpen] = useState(false);
    const anchorRef = useRef(null);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newQuery = event.target.value;
        setQuery(newQuery);
        setOpen(true);
        setQueryResult(searchResults(query))
        console.log("Query updated:", newQuery);
    };

    function searchResults(query: String): string[]{
        if (query.length === 0) return [];
        const sortedResults = new Map();
        const input = query.toLowerCase();
        let score = 1000;
        for (let i = 0; i < itemList.length; i++) {
            if (itemList[i].length < input.length-2) continue;
            const test = itemList[i].toLowerCase();

            if (test === input) {
                score = 0; // Exact match
            } else if (test.startsWith(input)) {
                score = 1; // Prefix-match
            } else if (test.includes(input)) {
                score = 2; // Partial match
            } else {
                score = 3 + distance(input, test); // Fuzzy match
            }
            sortedResults.set(itemList[i], score)
        }

        return getSmallestKeys(sortedResults)
    }

    function getSmallestKeys(map: Map<string, number>, count = 5): string[] {
        return [...map.entries()]
            .sort((a, b) => a[1] - b[1] || a[0].length - b[0].length) // Sort by values in ascending order
            .slice(0, count) // Get the first `count` entries
            .map(([key]) => key); // Extract and return sorted keys
    }

    return (
        <div>
            <OutlinedInput
                type="text"
                value={query}
                placeholder="Søk..."
                fullWidth
                onChange={handleChange}
                onBlur={() => setTimeout(()=> setOpen(false), 200)}
                inputRef={anchorRef}
                startAdornment = {<InputAdornment position="start"><SearchIcon/></InputAdornment>}
                sx={{
                    width: 300,
                    height: 45,
                    border: "2px solid #0B540D",
                    borderRadius: 5,
                    backgroundColor: "#F5F5F5",
                    "&:hover": {
                        backgroundColor: "#E0E0E0",
                    },
                    "& .MuiOutlinedInput-notchedOutline": {
                        border: "none",
                    },
                }}
            />
            <Popper open={open} anchorEl={anchorRef.current} placement="bottom-start" sx={{zIndex: 1000}}>
                <ClickAwayListener onClickAway={() => setOpen(false)}>
                    <Paper>
                        <List>
                            {queryResult.map((item) => (
                                <ListItem key={item}>
                                    <Link href={`/waste/${item}`}>{item}</Link>
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                </ClickAwayListener>
            </Popper>
        </div>
    )
}