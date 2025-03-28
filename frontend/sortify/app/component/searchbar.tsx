"use client";
import { distance } from "fastest-levenshtein"
import { useState, useRef , useEffect} from "react"
import { InputAdornment, OutlinedInput, Popper, Paper, List, ListItem, ClickAwayListener } from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"



export default function Searcbar(){

    const [wasteItems, setWasteItems] = useState<string[]>([]);

    useEffect(() => {
        const getWasteItems = async () =>{
            const response = await fetch("http://localhost:9876/api/waste");
            
            const data: string[] = await response.json();
    
            setWasteItems(data);
            
        };
        getWasteItems();
    }, []);
    



    const [query, setQuery] = useState("")
    const itemList = wasteItems;
//     ["Plastpose", "Plastflaske", "Plastbeger", "Plastbestikk", "Plastemballasje",
//     "Plastfolie", "Plastkork", "Mykplast", "Hardplast", "Isopor",
//     "Bobleplast", "Engangsplast", "PP-plast", "PET-flaske", "LDPE-plast", "PVC-rør",
  
//     "Papirpose", "Pappeske", "Avispapir", "Reklamepapir", "Kartong",
//     "Melkekartong", "Eggekartong", "Toalettpapirrull", "Tegnepapir",
//     "Konvolutt", "Bokomslag", "Bølgepapp", "Serviett", "Matpapir",
  
//     "Glassflaske", "Metallboks", "Hermetikkboks", "Syltetøyglass",
//     "Vinflaske", "Ølflaske", "Metallfolie", "Aluminiumsboks",
//     "Aluminiumsfolie", "Stålboks", "Lokk i metall", "Lysestake i glass",
//     "Speil", "Drikkeboks",
    
//     "Mobiltelefon", "Datamaskin", "Nettbrett", "Lader", "Batteri",
//     "Litiumbatteri", "LED-lyspære", "Halogenpære", "Lysrør",
//     "Kabel", "Høyttaler", "Hodetelefoner", "Fjernkontroll", "Strømledning",
  
//     "Klær", "Sko", "Gardiner", "Sengetøy", "Håndklær",
//     "Veske", "Belte", "Ullklær", "Jeans", "Skinnjakke",
//     "Pledd", "Dyne", "Teppe", "Lær",
  
//     "Maling", "Sprayboks", "Lakk", "Kjemikalier", "Rengjøringsmiddel",
//     "Løsemidler", "Neglelakk", "Neglelakkfjerner", "Lim", "Blekkpatron",
//     "Printerblekk", "Medisiner", "Desinfeksjonsmiddel", "Oljefilter",
  
//     "Matrester", "Banan", "Epleskrott", "Kjøttrester", "Brødskalk",
//     "Eggeskall", "Kaffegrut", "Tepose", "Fiskebein", "Grønnsaksskrell",
//     "Nøtteskall", "Potetskrell", "Skall fra sitrusfrukt",
  
//     "Gressklipp", "Løv", "Kvister", "Greiner", "Jord",
//     "Planter", "Blomster", "Ugress", "Busker", "Hageavfall",
  
//     "Stearinlys", "Tannbørste", "Q-tips", "Bomullspads",
//     "Engangshanske", "Bleie", "Bind", "Tampong", "Snus", "Sigarettsneip",
  
//     "Sofa", "Stol", "Bord", "Kommode", "Skap",
//     "Seng", "Madrass", "Teppe", "Speil", "Sykkel",
//     "Barnevogn", "TV", "Høyttaler"
//   ];
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
                score = 0; // Eksakt match (best mulig treff)
            } else if (test.startsWith(input)) {
                score = 1; // Prefiks-match
            } else if (test.includes(input)) {
                score = 2; // Delvis match
            } else {
                score = 3 + distance(input, test); // Fuzzy match
            }
            sortedResults.set(itemList[i], score) 
            
        }
        
        return getSmallestKeys(sortedResults)
    };

    function getSmallestKeys(map: Map<string, number>, count = 5): string[] {
        return [...map.entries()]
          .sort((a, b) => a[1] - b[1] || a[0].length - b[0].length) // Sort by values in ascending order
          .slice(0, count) // Get the first `count` entries
          .map(([key]) => key); // Extract and return sorted keys
      };


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
            />
            <Popper open={open} anchorEl={anchorRef.current} placement="bottom-start" sx={{zIndex: 1000}}>
                <ClickAwayListener onClickAway={() => setOpen(false)}>
                <Paper>
                    <List>
                        {queryResult.map((item) => (
                            <ListItem key={item}>
                                {item}
                            </ListItem>
                        ))}
                    </List>
                </Paper>
                </ClickAwayListener>
            </Popper>
        </div>
    )
}