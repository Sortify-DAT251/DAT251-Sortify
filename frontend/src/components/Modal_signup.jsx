import React, { useState } from 'react';
import { Modal, Box, Typography, TextField, Button, Grid, FormControlLabel, Checkbox } from '@mui/material';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,  // Adjust width for more space
    bgcolor: "#87C75C",
    border: '2px solid #000',
    borderRadius: '8px',  // Rounded corners
    boxShadow: 24,
    p: 4,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center', // Center the content vertically
    alignItems: 'center',
};

const inputStyle = {
    width: '45%', // Make each input take 45% of the width
    marginBottom: '16px',
};

export default function SignupModal() {
    const [open, setOpen] = useState(false);
    const [isSignup, setIsSignup] = useState(false);  // Toggle between signup and signin form

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleToggleForm = () => setIsSignup(!isSignup);

    return (
        <div style={{ marginLeft: "20px" }}>
            <Button variant="contained" color="primary" onClick={handleOpen}>Sign Up/In</Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2" sx={{ marginBottom: 2 }}>
                        {isSignup ? 'Sign Up for an account' : 'Sign In to your account'}
                    </Typography>

                    <Grid container spacing={2} justifyContent="center">
                        {/* Username/Email Input */}
                        <Grid item xs={5} sx={inputStyle}>
                            <TextField
                                label="Username/Email"
                                variant="filled"
                                fullWidth
                                required
                                InputProps={{
                                    style: {
                                        backgroundColor: '#ffffff', // White background
                                        border: '2px solid green',  // Black border around input field
                                        borderRadius: '8px',
                                    },
                                }}
                                InputLabelProps={{
                                    style: {
                                        color: 'darkgreen', // Change label color to black
                                    },
                                }}
                            />
                        </Grid>

                        {/* Password Input */}
                        <Grid item xs={5} sx={inputStyle}>
                            <TextField
                                label="Password"
                                variant="filled"
                                fullWidth
                                required
                                type="password"
                                InputProps={{
                                    style: {
                                        backgroundColor: '#ffffff', // White background
                                        border: '2px solid green',  // Black border around input field
                                        borderRadius: '8px',
                                    },
                                }}
                                InputLabelProps={{
                                    style: {
                                        color: 'darkgreen', // Change label color to black
                                    },
                                }}
                            />
                        </Grid>
                    </Grid>



                    {/* Sign-In Button */}
                    <Button
                        variant="contained"
                        sx={{
                            backgroundColor: '#ffffff',  // White background
                            color: 'black',  // Black text
                            border: '2px solid black',  // Optional: adds a black border
                            marginTop: 2,
                            '&:hover': {
                                backgroundColor: '#87C75C',  // Lighter background color on hover
                                borderColor: '#000000',  // Ensures the border stays black on hover
                                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',  // Adds shadow for depth
                            },
                        }}
                    >
                        {isSignup ? 'Sign Up' : 'Sign In'}
                    </Button>


                    {/* Don't have an account toggle */}
                    <Button
                        variant="text"
                        color="secondary"
                        fullWidth
                        sx={{ marginTop: 2 }}
                        onClick={handleToggleForm}
                    >
                        {isSignup ? 'Already have an account?' : 'Don\'t have an account?'}
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}
