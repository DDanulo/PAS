import {createContext, ReactNode, useContext, useEffect, useMemo, useState} from "react";
import {jwtDecode} from "jwt-decode";

interface JwtPayload {
    sub: string;
    role: string;
    exp: number;
}

interface AuthContextType {
    token: string | null;
    refreshToken: string | null;
    setTokens: (accessToken: string | null, refreshToken: string | null) => void;
    userRole: string | null;
    userLogin: string | null;
    isAuthenticated: boolean;
    logout: () => void;
}

const getInitialData = () => {
    const token = sessionStorage.getItem("access_token");
    if (token && token !== "undefined" && token !== "null") {
        try {
            const decoded = jwtDecode<JwtPayload>(token);
            return { role: decoded.role, login: decoded.sub };
        } catch {
            return { role: null, login: null };
        }
    }
    return { role: null, login: null };
};

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({children}: { children: ReactNode }) => {
    const initialState = getInitialData();

    const [token, setToken] = useState<string | null>(sessionStorage.getItem("access_token"));
    const [refreshToken, setRefreshToken] = useState<string | null>(sessionStorage.getItem("refresh_token"));

    const [userRole, setUserRole] = useState<string | null>(initialState.role);
    const [userLogin, setUserLogin] = useState<string | null>(initialState.login);

    const setTokens = (accessToken: string | null, rToken: string | null) => {
        if (accessToken) {
            sessionStorage.setItem('access_token', accessToken);
            setToken(accessToken);
        }
        if (rToken) {
            sessionStorage.setItem('refresh_token', rToken);
            setRefreshToken(rToken);
        }
    };

    const logout = () => {
        sessionStorage.clear();
        setToken(null);
        setRefreshToken(null);
        setUserRole(null);
        setUserLogin(null);
    };

    useEffect(() => {
        if (token && token !== "undefined" && token !== "null") {
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                setUserRole(decoded.role);
                setUserLogin(decoded.sub);
            } catch (e) {
                console.error("Błąd dekodowania JWT:", e);
                logout();
            }
        }
    }, [token]);

    const contextValue = useMemo(() => ({
        token,
        refreshToken,
        setTokens,
        userRole,
        userLogin,
        isAuthenticated: !!token,
        logout,
    }), [token, refreshToken, userRole, userLogin]);

    return <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used within AuthProvider");
    return context;
};

export default AuthProvider;