import { Height } from "@mui/icons-material";
import { createTheme, ThemeOptions } from "@mui/material";
import { DataGridProps } from "@mui/x-data-grid";

const customTheme: ThemeOptions & {
  components: {
    MuiDataGrid?: {
      styleOverrides?: {
        root?: object;
        columnHeaders?: object;
        row?: object;
      };
      defaultProps?: Partial<DataGridProps>;
    };
  };
} = {
  palette: {
    mode: "light",
    background: {
      default: "#F5F5F7",
    },
  },
  typography: {
    fontFamily: '"SUIT", sans-serif',
  },
  components: {
    MuiTypography: {
      styleOverrides: {
        h4: {
          fontWeight: 700,
        },
      },
    },
    MuiDrawer: {
      styleOverrides: {
        paper: {
          backgroundColor: "#202B45",
          color: "#FFFFFF",
          fontWeight: 400,
          fontSize: "16px",
          lineHeight: "150%",
          borderRadius: "8px",
          padding: "8px",
          height: "98%",
        },
        root: {
          "&.MuiDrawer-root .MuiPaper-root .MuiBox-root": {
            height: "auto",
            minHeight: "auto",
          },
          "&.MuiDrawer-root .MuiPaper-root .MuiBox-root .MuiList-root": {
            marginBottom: '16px',
          },
          "&.MuiDrawer-root .MuiPaper-root .MuiBox-root .MuiStack-root hr": {
            display: "none",
          },
          "&.MuiDrawer-root .MuiPaper-root .MuiBox-root .MuiStack-root button": {
            border: "1px solid #D3D5DB", 
            color: "#ffffff",
            "&:hover": {
              backgroundColor: "#4E546B",
            },
          },
        },
      },
    },
    MuiListItemButton: {
      styleOverrides: {
        root: {
          "& .MuiSvgIcon-root": {
            color: "#ffffff !important",
          },
          "&.Mui-selected": {
            backgroundColor: "#4E546B",
            "&:hover": {
              backgroundColor: "#4E546B",
            },
            "& .MuiTypography-root": {
              fontWeight: "bold !important",
            },
            "& .MuiSvgIcon-root": {
              color: "#FFFFFF !important",
            },
          },
        },
      },
    },
    MuiListItemIcon: {
      styleOverrides: {
        root: {
          color: "#ffffff",
        },
      },
    },
    MuiSvgIcon: {
      styleOverrides: {
        root: {
          // color: "#FF8400",
        },
      },
    },
    MuiListItemText: {
      styleOverrides: {
        primary: {
          "&.MuiTypography-root": {
            color: "inherit",
          },
        },
      },
    },
    MuiCheckbox: {
      styleOverrides: {
        root: {
          "& .MuiSvgIcon-root": {
            color: "#FF8400",
          },
          "&.Mui-checked .MuiSvgIcon-root": {
            color: "#FF8400",
          },
        },
      },
    },
    MuiDivider: {
      styleOverrides: {
        root: {
          backgroundColor: "#4E546B",
          width: "90%",
        },
      },
    },
    MuiDataGrid: {
      styleOverrides: {
        root: {
          backgroundColor: "#FFFFFF", 
          border: "none",
          "& .MuiDataGrid-columnHeaderCheckbox svg": {
            visibility: "hidden",
            pointerEvents: "none",
          },
          "& .MuiDataGrid-footerContainer": {
            display: "flex",
            justifyContent: "center", 
            alignItems: "center",
          },
          "& .MuiTablePagination-root": {
            display: "flex",
            justifyContent: "center", 
            width: "100%",
          },
          "& .MuiTablePagination-toolbar": {
            marginTop: "0px",
          },
          "& .MuiDataGrid-columnHeaderCheckbox .MuiSvgIcon-root": {
            color: "#DEDEDE",
          },
          "& .MuiCheckbox-root .MuiSvgIcon-root": {
            color: "#DEDEDE",
          },
        },
        columnHeaders: {
          backgroundColor: "#F5F5F7",
          color: "#333333", 
          fontWeight: "bold",
        },
        row: {
          "&:hover": {
            backgroundColor: "#F0F0F0", 
          },
        },
        
      },
      defaultProps: {
        disableColumnMenu: true,
        autoHeight: true,
      } as Partial<DataGridProps>,
    },
    MuiButton: {
      styleOverrides: {
        root: {
          "&.MuiButton-outlined": {
            borderColor: "#FF8400",
            color: "#FF8400",
            "&:hover": {
              backgroundColor: "rgba(255, 132, 0, 0.1)",
              borderColor: "#FF8400",
            },
          },
    
          "&.MuiButton-containedPrimary": {
            backgroundColor: "#FF8400",
            color: "#FFFFFF",
            "&:hover": {
              backgroundColor: "#E67500",
            },
          },
    
          "&.MuiButton-containedSecondary": {
            backgroundColor: "#000000",
            borderColor: "#000000",
            color: "#FFFFFF",
            "&:hover": {
              backgroundColor: "#333333",
              borderColor: "#333333",
            },
          },
    
          "&.Mui-disabled": {
            backgroundColor: "#D3D3D3", 
            color: "#FFFFFF", 
            borderColor: "#D3D3D3", 
          },
    
          "&.MuiButton-containedError": {
            backgroundColor: "#ED207B",
            color: "#FFFFFF",
            "&:hover": {
              backgroundColor: "#D81B6E",
            },
            "&.Mui-disabled": {
              backgroundColor: "#D3D3D3 !important", 
              color: "#FFFFFF !important", 
            },
          },
          "&.MuiButton-outlinedError": {
            borderColor: "#ED207B",
            color: "#ED207B",
            "&:hover": {
              backgroundColor: "rgba(237, 32, 123, 0.1)",
            },
            "&.Mui-disabled": {
              backgroundColor: "#D3D3D3 !important",
              color: "#FFFFFF !important",
              borderColor: "#D3D3D3 !important",
            },
          },
        },
      },
    },
  },
};

export default createTheme(customTheme);
