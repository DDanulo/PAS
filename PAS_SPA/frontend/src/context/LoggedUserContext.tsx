import axios from "axios";
import {createContext, useContext, useEffect, useMemo, useState, ReactNode} from "react";
import {jwtDecode} from "jwt-decode";

interface JwtPayload {
    sub: string;
    role: string;
    exp: number;
}

interface AuthContextType {
    token: string | null;
    setToken: (token: string | null) => void;
    userRole: string | null;
    userLogin: string | null;
    isAuthenticated: boolean;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({children}: { children: ReactNode }) => {
    const [token, setToken_] = useState<string | null>(sessionStorage.getItem("jwt_token"));
    const [userRole, setUserRole] = useState<string | null>(null);
    const [userLogin, setUserLogin] = useState<string | null>(null);

    const setToken = (newToken: string | null) => {
        setToken_(newToken);
    };

    const logout = () => {
        setToken(null);
    };

    useEffect(() => {
        if (token) {
            axios.defaults.headers.common["Authorization"] = "Bearer " + token;
            sessionStorage.setItem('jwt_token', token);

            try {
                const decoded = jwtDecode<JwtPayload>(token);
                setUserRole(decoded.role);
                setUserLogin(decoded.sub);
            } catch (e) {
                setToken(null);
            }

        } else {
            delete axios.defaults.headers.common["Authorization"];
            sessionStorage.removeItem('jwt_token');
            setUserRole(null);
            setUserLogin(null);
        }
    }, [token]);

    const contextValue = useMemo(
        () => ({
            token,
            setToken,
            userRole,
            userLogin,
            isAuthenticated: !!token,
            logout,
        }),
        [token, userRole, userLogin]
    );

    return (
        <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error;
    }
    return context;
};

export default AuthProvider;