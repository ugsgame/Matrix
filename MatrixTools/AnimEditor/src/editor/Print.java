/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package editor;
/**
 *
 * @author ni
 */
public final class Print {

    public static void printArray(int a[], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {

            if (i < a.length - 1) {
                System.out.println("" + a[i] + ",");
            } else {
                System.out.println("" + a[i]);
            }
        }
        System.out.println(" }");
        System.out.println("length=" + a.length);

    }

    public static void printArray(byte a[], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {

            if (i < a.length - 1) {
                System.out.println("" + a[i] + ",");
            } else {
                System.out.println("" + a[i]);
            }
        }
        System.out.println(" }");
        System.out.println("length=" + a.length);

    }

    public static void printArray(int a[][][][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int m = 0; m < a.length; m++) {
            String p = "  {";
            System.out.println(p);
            for (int i = 0; a[m] != null && i < a[m].length; i++) {
                String s = "    {";
                System.out.println(s);
                for (int j = 0; a[m][i] != null && j < a[m][i].length; j++) {
                    String t = "      {";
                    for (int k = 0; a[m][i][j] != null && k < a[m][i][j].length; k++) {
                        if (k < a[m][i][j].length - 1) {
                            t += "" + a[m][i][j][k] + ",";
                        } else {
                            t += "" + a[m][i][j][k];
                        }
                    }
                    t += "}";
                    if (a[m][i] != null && j < a[m][i].length - 1) {
                        System.out.println(t + ",");
                    } else {
                        System.out.println(t);
                    }
                }//end for
                s = "    }";
                if (i < a[m].length - 1) {
                    System.out.println(s + ",");
                } else {
                    System.out.println(s);
                }
            }//end for




            p = "  }";
            if (m < a.length - 1) {
                System.out.println(p + ",");
            } else {
                System.out.println(p);
            }
        }
        System.out.println("}");
        System.out.println("length=" + a.length);
    }

    public static void printArray(int a[][][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            System.out.println(s);
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                String t = "    {";
                for (int k = 0; a[i][j] != null && k < a[i][j].length; k++) {
                    if (k < a[i][j].length - 1) {
                        t += "" + a[i][j][k] + ",";
                    } else {
                        t += "" + a[i][j][k];
                    }
                }
                t += " }";
                if (a[i] != null && j < a[i].length - 1) {
                    System.out.println(t + ",");
                } else {
                    System.out.println(t);
                }
            }//end for
            s = "  }";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }//end for
        System.out.println("}");
        System.out.println("length=" + a.length);

    }

    public static void printArray(int a[][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                if (j < a[i].length - 1) {
                    s += "" + a[i][j] + ",";
                } else {
                    s += "" + a[i][j];
                }
            }
            s += "}";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }
        System.out.println("}");
        System.out.println("length=" + a.length);


    }

    public static void printArray(byte a[][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                if (j < a[i].length - 1) {
                    s += "" + a[i][j] + ",";
                } else {
                    s += "" + a[i][j];
                }
            }
            s += "}";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }
        System.out.println("}");
        System.out.println("length=" + a.length);


    }

    public static void printArray(short a[][][][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int m = 0; m < a.length; m++) {
            String p = "  {";
            System.out.println(p);
            for (int i = 0; a[m] != null && i < a[m].length; i++) {
                String s = "    {";
                System.out.println(s);
                for (int j = 0; a[m][i] != null && j < a[m][i].length; j++) {
                    String t = "      {";
                    for (int k = 0; a[m][i][j] != null && k < a[m][i][j].length; k++) {
                        if (k < a[m][i][j].length - 1) {
                            t += "" + a[m][i][j][k] + ",";
                        } else {
                            t += "" + a[m][i][j][k];
                        }
                    }
                    t += "}";
                    if (a[m][i] != null && j < a[m][i].length - 1) {
                        System.out.println(t + ",");
                    } else {
                        System.out.println(t);
                    }
                }//end for
                s = "    }";
                if (i < a[m].length - 1) {
                    System.out.println(s + ",");
                } else {
                    System.out.println(s);
                }
            }//end for




            p = "  }";
            if (m < a.length - 1) {
                System.out.println(p + ",");
            } else {
                System.out.println(p);
            }
        }
        System.out.println("}");
        System.out.println("length=" + a.length);
    }

    public static void printArray(short a[][][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            System.out.println(s);
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                String t = "    {";
                for (int k = 0; a[i][j] != null && k < a[i][j].length; k++) {
                    if (k < a[i][j].length - 1) {
                        t += "" + a[i][j][k] + ",";
                    } else {
                        t += "" + a[i][j][k];
                    }
                }
                t += " }";
                if (a[i] != null && j < a[i].length - 1) {
                    System.out.println(t + ",");
                } else {
                    System.out.println(t);
                }
            }//end for
            s = "  }";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }//end for
        System.out.println("}");
        System.out.println("length=" + a.length);

    }

    public static void printArray(byte a[][][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            System.out.println(s);
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                String t = "    {";
                for (int k = 0; a[i][j] != null && k < a[i][j].length; k++) {
                    if (k < a[i][j].length - 1) {
                        t += "" + a[i][j][k] + ",";
                    } else {
                        t += "" + a[i][j][k];
                    }
                }
                t += " }";
                if (a[i] != null && j < a[i].length - 1) {
                    System.out.println(t + ",");
                } else {
                    System.out.println(t);
                }
            }//end for
            s = "  }";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }//end for
        System.out.println("}");
        System.out.println("length=" + a.length);

    }

    public static void printArray(String a[][][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            System.out.println(s);
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                String t = "    {";
                for (int k = 0; a[i][j] != null && k < a[i][j].length; k++) {
                    if (k < a[i][j].length - 1) {
                        t += "" + a[i][j][k] + ",";
                    } else {
                        t += "" + a[i][j][k];
                    }
                }
                t += " }";
                if (a[i] != null && j < a[i].length - 1) {
                    System.out.println(t + ",");
                } else {
                    System.out.println(t);
                }
            }//end for
            s = "  }";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }//end for
        System.out.println("}");
        System.out.println("length=" + a.length);

    }

    public static void printArray(short a[][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                if (j < a[i].length - 1) {
                    s += "" + a[i][j] + ",";
                } else {
                    s += "" + a[i][j];
                }
            }
            s += "}";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }
        System.out.println("}");
        System.out.println("length=" + a.length);


    }

    public static void printArray(short a[], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {

            if (i < a.length - 1) {
                System.out.println("" + a[i] + ",");
            } else {
                System.out.println("" + a[i]);
            }
        }
        System.out.println(" }");
        System.out.println("length=" + a.length);
    }

    public static void printArray(String a[], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {

            if (i < a.length - 1) {
                System.out.println("" + a[i] + ",");
            } else {
                System.out.println("" + a[i]);
            }
        }
        System.out.println(" }");

        System.out.println("length=" + a.length);
    }

    public static void printArray(String a[][], String name) {
        if (a == null) {
            System.out.println(name + " is null!");
            return;
        }
        System.out.println(name + "={");
        for (int i = 0; i < a.length; i++) {
            String s = "  {";
            for (int j = 0; a[i] != null && j < a[i].length; j++) {
                if (j < a[i].length - 1) {
                    s += "" + a[i][j] + ",";
                } else {
                    s += "" + a[i][j];
                }
            }
            s += "}";
            if (i < a.length - 1) {
                System.out.println(s + ",");
            } else {
                System.out.println(s);
            }
        }
        System.out.println("}");
        System.out.println("length=" + a.length);
    }
    //add whb
}
