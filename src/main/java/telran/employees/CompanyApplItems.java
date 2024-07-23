package telran.employees;

import java.util.*;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class CompanyApplItems {
    static Company company;
    static HashSet<String> departments;

    public static List<Item> getCompanyItems(Company company, HashSet<String> departments) {
        CompanyApplItems.company = company;
        CompanyApplItems.departments = departments;
        Item[] items = {
            Item.of("add employee", CompanyApplItems::addEmployeeMenu),
            Item.of("display employee data", CompanyApplItems::getEmployee),
            Item.of("remove employee", CompanyApplItems::removeEmployee),
            Item.of("display department budget", CompanyApplItems::getDepartmentBudget),
            Item.of("display departments", CompanyApplItems::getDepartments),
            Item.of("display managers with most factor", CompanyApplItems::getManagersWithMostFactor),
        };
        return new ArrayList<>(List.of(items));
    }

    static void addEmployeeMenu(InputOutput io) {
        Item[] employeeItems = {
            Item.of("Add WageEmployee", CompanyApplItems::addWageEmployee),
            Item.of("Add SalesPerson", CompanyApplItems::addSalesPerson),
            Item.of("Add Manager", CompanyApplItems::addManager)
        };

        Menu employeeMenu = new Menu("Add Employee", employeeItems);
        employeeMenu.perform(io);
    }

    static void addWageEmployee(InputOutput io) {
        Employee empl = readEmployee(io);
        Employee wageEmployee = getWageEmployee(empl, io);
        company.addEmployee(wageEmployee);
        io.writeLine("WageEmployee has been added");
    }

    static void addSalesPerson(InputOutput io) {
        Employee empl = readEmployee(io);
        Employee salesPerson = getSalesPerson(empl, io);
        company.addEmployee(salesPerson);
        io.writeLine("SalesPerson has been added");
    }

    static void addManager(InputOutput io) {
        Employee empl = readEmployee(io);
        Employee manager = getManager(empl, io);
        company.addEmployee(manager);
        io.writeLine("Manager has been added");
    }

    private static Employee getSalesPerson(Employee empl, InputOutput io) {
        WageEmployee wageEmployee = (WageEmployee) getWageEmployee(empl, io);
        float percents = io.readNumberRange("Enter percents", "Wrong percents value", 0.5, 2).floatValue();
        long sales = io.readNumberRange("Enter sales", "Wrong sales value", 500, 50000).longValue();
        return new SalesPerson(empl.getId(), empl.getBasicSalary(), empl.getDepartment(),
                wageEmployee.getHours(), wageEmployee.getWage(), percents, sales);
    }

    private static Employee getManager(Employee empl, InputOutput io) {
        float factor = io.readNumberRange("Enter factor", "Wrong factor value", 1.5, 5).floatValue();
        return new Manager(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), factor);
    }

    private static Employee getWageEmployee(Employee empl, InputOutput io) {
        int hours = io.readNumberRange("Enter working hours", "Wrong hours value", 10, 200).intValue();
        int wage = io.readNumberRange("Enter hour wage", "Wrong wage value", 100, 1000).intValue();
        return new WageEmployee(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), hours, wage);
    }

    private static Employee readEmployee(InputOutput io) {
        long id = io.readNumberRange("Enter id value", "Wrong id value", 1000, 10000).longValue();
        int basicSalary = io.readNumberRange("Enter basic salary", "Wrong basic salary", 2000, 20000).intValue();
        String department = io.readStringOptions("Enter department " + departments, "Wrong department", departments);
        return new Employee(id, basicSalary, department);
    }

    static void getEmployee(InputOutput io) {
        long id = io.readLong("Enter employee ID", "Invalid ID");
        Employee employee = company.getEmployee(id);
        if (employee != null) {
            io.writeLine(employee);
        } else {
            io.writeLine("Employee not found");
        }
    }

    static void removeEmployee(InputOutput io) {
        long id = io.readLong("Enter employee ID", "Invalid ID");
        Employee removedEmployee = company.removeEmployee(id);
        if (removedEmployee != null) {
            io.writeLine("Employee removed: " + removedEmployee);
        } else {
            io.writeLine("Employee not found");
        }
    }

    static void getDepartmentBudget(InputOutput io) {
        String department = io.readStringOptions("Enter department", "Invalid department", departments);
        int budget = company.getDepartmentBudget(department);
        io.writeLine("Department budget: " + budget);
    }

    static void getDepartments(InputOutput io) {
        String[] departmentArray = company.getDepartments();
        io.writeLine("Departments: " + Arrays.toString(departmentArray));
    }

    static void getManagersWithMostFactor(InputOutput io) {
        Manager[] managers = company.getManagersWithMostFactor();
        io.writeLine("Managers with the highest factor: ");
        for (Manager manager : managers) {
            io.writeLine(manager);
        }
    }
}
