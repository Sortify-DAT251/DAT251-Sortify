"use client";
import { distance } from "fastest-levenshtein"
import { useState, useRef , useEffect} from "react"
import { InputAdornment, OutlinedInput, Popper, Paper, List, ListItem, ClickAwayListener } from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"
import styles from "./searchbar.module.css"
import { UUID } from "crypto";
import { useSearch } from "@/app/context/searchContext";


type wasteItem = {
    id: UUID,
    info: string,
    name: string,
    type: string
}

export default function Searcbar(){

    const { search, setSearch } = useSearch()

    const [wasteItems, setWasteItems] = useState<wasteItem[]>([]);

    useEffect(() => {
        const getWasteItems = async () =>{
            const response = await fetch("http://localhost:9876/api/waste");

            const data: wasteItem[] = await response.json();
            const items = data;
            setWasteItems(items);
            console.log("items:",items)
            console.log(items[0].name, items[0].info, items[0].type)

        };
        getWasteItems();
    }, []);




    const [query, setQuery] = useState("")
    //const itemList = wasteItems.map((item) => item.name);
    const [queryResult, setQueryResult] = useState<wasteItem[]>([])
    const [open, setOpen] = useState(false);
    const anchorRef = useRef(null);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newQuery = event.target.value;
        setQuery(newQuery);
        setOpen(true);
        setQueryResult(searchResults(query))
        console.log("Query updated:", newQuery);
    };

    function searchResults(query: String): wasteItem[]{
        if (query.length === 0) return [];
        const sortedResults = new Map();
        const input = query.toLowerCase();
        let score = 1000;
        for (let i = 0; i < wasteItems.length; i++) {
            if (wasteItems[i].name.length < input.length-2) continue;
            const test = wasteItems[i].name.toLowerCase();

            if (test === input) {
                score = 0; // Eksakt match (best mulig treff)
            } else if (test.startsWith(input)) {
                score = 1; // Prefiks-match
            } else if (test.includes(input)) {
                score = 2; // Delvis match
            } else {
                score = 3 + distance(input, test); // Fuzzy match
            }
            sortedResults.set(wasteItems[i], score)

        }

        return getSmallestKeys(sortedResults)
    };

    function getSmallestKeys(map: Map<wasteItem, number>, count = 5): wasteItem[] {
        return [...map.entries()]
            .sort((a, b) => a[1] - b[1] || a[0].name.length - b[0].name.length) // Sort by values in ascending order
            .slice(0, count) // Get the first `count` entries
            .map(([key]) => key); // Extract and return sorted keys
    };

    return (
        <div className={styles.searchbarWrapper}>
            <OutlinedInput
                type="text"
                value={query}
                placeholder="Fyll inn søppel her..."
                fullWidth
                onChange={handleChange}
                onBlur={() => setTimeout(()=> setOpen(false), 200)}
                inputRef={anchorRef}
                startAdornment = {<InputAdornment position="start"><SearchIcon/></InputAdornment>}
                className={styles.searchbarContainer}
                sx={{
                    borderRadius: '40px',
                }}
            />
            <Popper open={open} anchorEl={anchorRef.current} placement="bottom-start" sx={{zIndex: 1000}}>
                <ClickAwayListener onClickAway={() => setOpen(false)}>
                    <Paper>
                        <List>
                            {queryResult.map((item) => (
                                <ListItem
                                    key={item.name}
                                    className={styles.listItem}
                                    onClick={() => setSearch(item.type)}
                                >
                                    <p>{item.name}</p>
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                </ClickAwayListener>
            </Popper>
        </div>
    )
}