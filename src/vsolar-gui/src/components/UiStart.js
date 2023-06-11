import '../styles/uiStart.css'
import icon from '../media/icons/icon.png';
import React, { useEffect, useState } from 'react';


export const UiStart = () => {
    const [isIpAvailable, setIsIpAvailable] = useState(false);

    useEffect(() => {
        const checkIpAvailability = async () => {
            try {
                const response = await fetch('https://10.128.250.32');
                if (response.ok) {
                    setIsIpAvailable(true);
                } else {
                    setIsIpAvailable(false);
                }
            } catch (error) {
                setIsIpAvailable(false);
            }
        };

        checkIpAvailability();
    }, []);

    return (
        <div className="outer">
            <div className="outline">
                <img src={icon} alt="Icon" />
                <p>status: {isIpAvailable ? "ok" : "no connection"}</p>
                <button>start client</button>
            </div>
        </div>
    );
};